package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResTradeDetailDTO {
    private Long tradeId;
    private Long requesterId;
    private String requesterName;
    private String requesterAvatar;
    private Long ownerId;
    private String ownerName;
    private String ownerAvatar;
    private Long itemRequesterId;
    private String itemRequesterTitle;
    private String itemRequesterImage;
    private Long itemOwnerId;
    private String itemOwnerTitle;
    private String itemOwnerImage;

    private boolean canUpdate;

}
