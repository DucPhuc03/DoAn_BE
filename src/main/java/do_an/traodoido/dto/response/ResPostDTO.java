package do_an.traodoido.dto.response;

import do_an.traodoido.enums.PostStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ResPostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String description;
    private String itemCondition;
    private LocalDate postDate;
    private String tradeLocation;
    private PostStatus postStatus;
    private List<String> imageUrls;
    private String categoryName;
    private Long categoryId;
}
