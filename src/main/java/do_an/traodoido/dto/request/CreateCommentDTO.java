package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class CreateCommentDTO {
    private Long postId;
    private Long userId;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate commentDate;
}
