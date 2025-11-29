package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStatusPost {
    private Long postId;
    private String status;
}
