package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTradePostDTO {
    private Long tradeId;
    private Long requesterPostId;
}
