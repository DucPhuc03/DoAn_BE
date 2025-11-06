package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.ResMessageDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Conversation;
import do_an.traodoido.entity.Message;
import do_an.traodoido.repository.ConversationRepository;
import do_an.traodoido.service.ConversationService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService  {
    private final ConversationRepository conversationRepository;
    private final UserService userService;

    @Override
    public RestResponse<List<ResConversationDTO>> getConversationIds() {
        Long currentUserId = userService.getCurrentUserId();
        List<Conversation> conversations = conversationRepository
                .findByParticipant1_IdOrParticipant2_Id(currentUserId, currentUserId);

        List<ResConversationDTO> data = conversations.stream()
                .map(conversation -> ResConversationDTO.builder()
                        .conversationId(conversation.getId())
                        .itemTitle(
                                conversation.getTrade() != null
                                        && conversation.getTrade().getOwnerPost() != null
                                        ? conversation.getTrade().getOwnerPost().getTitle()
                                        : null
                        )
                        .messages(mapMessages(conversation.getMessages()))
                        .build())
                .collect(Collectors.toList());

        return RestResponse.<List<ResConversationDTO>>builder()
                .code(200)
                .message("OK")
                .data(data)
                .build();
    }

    private List<ResMessageDTO> mapMessages(List<Message> messages) {
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .map(m -> ResMessageDTO.builder()
                        .id(m.getId())
                        .senderName(m.getSender() != null ? m.getSender().getFullName() : null)
                        .avatarUrl(m.getSender() != null ? m.getSender().getAvatarUrl() : null)
                        .timestamp(m.getTimestamp())
                        .content(m.getContent())
                        .isRead(m.isRead())
                        .build())
                .collect(Collectors.toList());
    }
}
