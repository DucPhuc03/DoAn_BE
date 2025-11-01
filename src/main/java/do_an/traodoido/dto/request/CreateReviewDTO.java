package do_an.traodoido.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewDTO {
    private Long reviewedId;
    private int rating;
    private String comment;
    private Long tradeId;
}
