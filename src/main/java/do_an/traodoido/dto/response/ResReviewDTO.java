package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ResReviewDTO {
    private Long id;
    private String content;
    private int rating;
    private LocalDate reviewDate;
    private Long reviewerId;
    private String reviewerName;
    private String reviewerAvatar;

    private Long tradeId;
    private String itemTitle;
    private String itemImage;
}
