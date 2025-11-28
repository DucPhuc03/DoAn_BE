package do_an.traodoido.controller;

import do_an.traodoido.dto.request.ChatMessageDTO;
import do_an.traodoido.dto.response.ResMessageDTO;
import do_an.traodoido.entity.Conversation;
import do_an.traodoido.entity.Message;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.ConversationRepository;
import do_an.traodoido.repository.MessageRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    @SendTo("/chat-trade/{conversationId}") // Gửi tin nhắn đến broker
    public ResMessageDTO sendMessage(
            @DestinationVariable Long conversationId,
            @Payload ChatMessageDTO chatMessage) {
        User currentUser = userRepository.findById(chatMessage.getSenderId()).orElseThrow();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new InvalidException("Conversation not found with id: " + conversationId));
        Message message = Message.builder()
                .conversation(conversation)
                .sender(currentUser)
                .content(chatMessage.getContent())
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);


        return ResMessageDTO.builder()
                .id(message.getId())
                .senderId(currentUser.getId())
                .avatarUrl(currentUser.getAvatarUrl())
                .senderName(currentUser.getFullName())
                .timestamp(message.getTimestamp())
                .content(chatMessage.getContent())
                .isRead(message.isRead())
                .build();
    }

    @PostMapping("/api/chat/{conversationId}/messages")
    public ResMessageDTO sendMessageRest(
            @PathVariable Long conversationId,
            @RequestBody ChatMessageDTO request) {
        User currentUser = userService.getCurrentUser();


        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new InvalidException("Conversation not found with id: " + conversationId));
        Message message = Message.builder()
                .conversation(conversation)
                .sender(currentUser)
                .content(request.getContent())
                .isRead(false)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);


        return ResMessageDTO.builder()
                .id(message.getId())
                .avatarUrl(currentUser.getAvatarUrl())
                .senderName(currentUser.getFullName())
                .timestamp(message.getTimestamp())
                .content(message.getContent())
                .isRead(message.isRead())
                .build();
    }
}
