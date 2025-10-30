package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewDTO {
    private Long postId;
    private Long reviewedId;
    private int rating;
    private String comment;
    private Long tradeId;
}
