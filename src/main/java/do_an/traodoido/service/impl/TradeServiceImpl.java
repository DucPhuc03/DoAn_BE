package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.request.TradeNotificationPayload;
import do_an.traodoido.dto.request.UpdateTradePostDTO;
import do_an.traodoido.dto.response.ResTradeDetailDTO;
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
    private  final SimpMessageSendingOperations messagingTemplate;

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

//        messagingTemplate.convertAndSendToUser(
//                String.valueOf(userOwner.getId()),
//                "/queue/notification",
//                payload
//        );
        return RestResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data("Trade created successfully").build();
    }

    @Override
    public RestResponse<String> updateTradeStatus(Long tradeId, String status) {

        return null;
    }

    @Override
    public RestResponse<String> updateRequesterPost(UpdateTradePostDTO updateTradePostDTO) {
        Trade trade = tradeRepository.findById(updateTradePostDTO.getTradeId()).orElseThrow(()->new InvalidException("Trade not found"));
        Post requesterPost = postRepository.findById(updateTradePostDTO.getRequesterPostId()).orElseThrow(()->new InvalidException("Requester post not found"));

        trade.setRequesterPost(requesterPost);
        tradeRepository.save(trade);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data("Requester post updated successfully").build();
    }

    @Override
    public RestResponse<ResTradeDetailDTO> getTradeDetails(Long tradeId) {

        Long currentUserId = userService.getCurrentUserId();
        boolean canUpdate = false;
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(()->new InvalidException("Trade not found"));
        if(!trade.getOwner().getId().equals(currentUserId)){
            canUpdate=true;
        }
        ResTradeDetailDTO data = ResTradeDetailDTO.builder()
                .tradeId(trade.getId())
                .requesterId(trade.getRequester().getId())
                .requesterName(trade.getRequester().getFullName())
                .requesterAvatar(trade.getRequester().getAvatarUrl())
                .ownerId(trade.getOwner().getId())
                .ownerName(trade.getOwner().getFullName())
                .ownerAvatar(trade.getOwner().getAvatarUrl())
                .itemRequesterId(
                        trade.getRequesterPost() != null
                                ? trade.getRequesterPost().getId()
                                : null
                )
                .itemRequesterTitle(
                        trade.getRequesterPost() != null
                                ? trade.getRequesterPost().getTitle()
                                : null
                )
                .itemRequesterImage(
                        trade.getRequesterPost() != null && !trade.getRequesterPost().getImages().isEmpty()
                                ? trade.getRequesterPost().getImages().get(0).getImageUrl()
                                : null
                )
                .itemOwnerId(
                        trade.getOwnerPost() != null
                                ? trade.getOwnerPost().getId()
                                : null
                )
                .itemOwnerTitle(
                        trade.getOwnerPost() != null
                                ? trade.getOwnerPost().getTitle()
                                : null
                )
                .itemOwnerImage(
                        trade.getOwnerPost() != null && !trade.getOwnerPost().getImages().isEmpty()
                                ? trade.getOwnerPost().getImages().get(0).getImageUrl()
                                : null
                )
                .canUpdate(canUpdate)

                .build();

        return RestResponse.<ResTradeDetailDTO>builder()
                .code(1000)
                .message("Success")
                .data(data)
                .build();
    }

}
