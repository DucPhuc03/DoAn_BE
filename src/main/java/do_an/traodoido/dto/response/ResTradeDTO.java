package do_an.traodoido.dto.response;

import do_an.traodoido.enums.TradeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResTradeDTO {
    private Long tradeId;
    private Long userId;
    private String userName;
    private String userAvatar;
    private String requesterPostTitle;
    private String requesterPostImage;
    private String ownerPostTitle;
    private String ownerPostImage;
    private TradeStatus status;

    private boolean canRate;
}
