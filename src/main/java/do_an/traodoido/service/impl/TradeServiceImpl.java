package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.request.TradeNotificationPayload;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Conversation;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.Trade;
import do_an.traodoido.entity.User;
import do_an.traodoido.enums.TradeStatus;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.ConversationRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.TradeService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
    private final TradeRepository tradeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private SimpMessageSendingOperations messagingTemplate;

    public RestResponse<String> createTrade(CreateTradeDTO createTradeDTO){
        User userRequester = userService.getCurrentUser();
        Post ownerPost = postRepository.findById(createTradeDTO.getOwnerPostId()).orElseThrow(()->new InvalidException("Owner post not found"));
        User userOwner = userRepository.findById(createTradeDTO.getUserOwnerId()).orElseThrow(()->new InvalidException("User owner not found"));
        Trade trade = Trade.builder()
                .ownerPost(ownerPost)
                .owner(userOwner)
                .requester(userRequester)
                .tradeStatus(TradeStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        tradeRepository.save(trade);
        Conversation conversation = Conversation.builder()
                .participant1(userRequester)
                .participant2(userOwner)
                .trade(trade)
                .build();

        conversationRepository.save(conversation);

        TradeNotificationPayload payload = TradeNotificationPayload.builder()
                .tradeId(trade.getId())
                .requesterName(userRequester.getFullName())
                .conversationId(conversation.getId())
                .build();

        messagingTemplate.convertAndSendToUser(
                String.valueOf(userOwner.getId()),
                "/queue/notification",
                payload
        );
        return RestResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data("Trade created successfully").build();
    }

}
