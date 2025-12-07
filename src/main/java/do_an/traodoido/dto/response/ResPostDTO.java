package do_an.traodoido.dto.response;

import do_an.traodoido.entity.Category;
import do_an.traodoido.enums.PostStatus;
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
    private PostStatus postStatus;
    private int totalLikes;
    private double distance;
    private Category category;
}
