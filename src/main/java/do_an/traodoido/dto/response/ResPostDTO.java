package do_an.traodoido.dto.response;

import do_an.traodoido.entity.Category;
import do_an.traodoido.entity.Comment;
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
    private Category category;
    private List<String> imageUrls;
    private List<CommentDTO> comments;

    private boolean isLiked;
    private boolean canEdit;
    private boolean canDelete;
    private boolean canReport;
    private boolean canUpdateStatus;
    private int totalLikes;
    private int totalComments;
}
