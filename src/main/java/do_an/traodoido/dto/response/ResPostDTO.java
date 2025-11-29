package do_an.traodoido.dto.response;

import do_an.traodoido.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ResPostDTO {
    private Long id;
    private String title;
    private String username;
    private LocalDate postDate;
    private String imageUrl;
    private int totalLikes;
    private Category category;
}
