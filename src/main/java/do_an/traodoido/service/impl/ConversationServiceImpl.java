package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.ResMessageDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.UserChat;
import do_an.traodoido.entity.*;
import do_an.traodoido.enums.PostStatus;
import do_an.traodoido.enums.TradeStatus;
import do_an.traodoido.repository.ConversationRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.service.ConversationService;
import do_an.traodoido.service.MeetingService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationServiceImpl implements ConversationService  {
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final MeetingService meetingService;
    private final TradeRepository tradeRepository;
    private final PostRepository postRepository;

    @Override
    public RestResponse<Map<UserChat,List<ResConversationDTO>>> getConversationIds() {
        Long currentUserId = userService.getCurrentUserId();
        List<Conversation> conversations = conversationRepository
                .findByParticipant1_IdOrParticipant2_IdOrderByIdDesc(currentUserId, currentUserId);
        List<ResConversationDTO> data = conversations.stream()
                .filter(conversation ->!conversation.getTrade().getTradeStatus().equals(TradeStatus.COMPLETED))
                .map(conversation -> ResConversationDTO.builder()
                        .conversationId(conversation.getId())
                        .tradeId(conversation.getTrade() != null ? conversation.getTrade().getId() : null)
                        .partner(UserChat.builder()
                                .userId(
                                        conversation.getParticipant1().getId().equals(currentUserId)
                                                ? conversation.getParticipant2().getId()
                                                : conversation.getParticipant1().getId()
                                )
                                .username(
                                        conversation.getParticipant1().getId().equals(currentUserId)
                                                ? conversation.getParticipant2().getFullName()
                                                : conversation.getParticipant1().getFullName()
                                )
                                .avatarUrl(
                                        conversation.getParticipant1().getId().equals(currentUserId)
                                                ? conversation.getParticipant2().getAvatarUrl()
                                                : conversation.getParticipant1().getAvatarUrl()
                                )
                                .build())
                        .itemTitle(
                                conversation.getTrade() != null
                                        && conversation.getTrade().getOwnerPost() != null
                                        ? conversation.getTrade().getOwnerPost().getTitle()
                                        : null
                        )
                        .itemImage(conversation.getTrade() != null
                                && conversation.getTrade().getOwnerPost() != null && conversation.getTrade().getOwnerPost().getImages() != null
                                ? conversation.getTrade().getOwnerPost().getImages().stream()
                                    .findFirst()
                                    .map(Image::getImageUrl)
                                    .orElse(null)
                                : null)
                        .messages(mapMessages(conversation.getMessages(),currentUserId))
                        .meeting(meetingService.getMeetingTrade(
                                conversation.getTrade() != null ? conversation.getTrade().getId() : null
                        ))
                        .build())
                .toList();
        Map<UserChat, List<ResConversationDTO>> groupedByTradeId = data.stream()
                .collect(Collectors.groupingBy(ResConversationDTO::getPartner));
        return RestResponse.<Map<UserChat,List<ResConversationDTO>>>builder()
                .code(200)
                .message("OK")
                .data(groupedByTradeId)
                .build();
    }

    @Override
    @Transactional
    public void deleteConversation(Long id) {
        Conversation conversation=conversationRepository.findById(id).orElseThrow();
        Post owner=conversation.getTrade().getOwnerPost();
        owner.setPostStatus(PostStatus.AVAILABLE);
        postRepository.save(owner);
        Optional<Post> requester= Optional.ofNullable(conversation.getTrade().getRequesterPost());
        if(requester.isPresent()){
            requester.get().setPostStatus(PostStatus.AVAILABLE);
            postRepository.save(requester.get());
        }
        tradeRepository.deleteById(id);
        conversationRepository.delete(conversation);

    }

    private List<ResMessageDTO> mapMessages(List<Message> messages,Long id) {
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .map(m -> ResMessageDTO.builder()
                        .id(m.getId())
                        .senderName(m.getSender() != null ? m.getSender().getFullName() : null)
                        .senderId(m.getSender() != null ? m.getSender().getId() : null)
                        .avatarUrl(m.getSender() != null ? m.getSender().getAvatarUrl() : null)
                        .timestamp(m.getTimestamp())
                        .content(m.getContent())
                        .isMe(m.getSender().getId().equals(id))
                        .isRead(m.isRead())
                        .build())
                .collect(Collectors.toList());
    }
}
