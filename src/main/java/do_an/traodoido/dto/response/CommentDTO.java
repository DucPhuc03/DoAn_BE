package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private String content;
    private LocalDate commentDate;
}
