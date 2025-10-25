package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreatePostDTO {
    private String title;
    private String description;
    private String itemCondition;
    private LocalDate postDate;
    private String tradeLocation;
    private Long categoryId;
    private Long userId;
}
