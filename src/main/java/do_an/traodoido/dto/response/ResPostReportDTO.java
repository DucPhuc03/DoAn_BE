package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.services.bedrockruntime.endpoints.internal.Value;

import java.util.List;

@Data
@Builder

public class ResPostReportDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private String postTitle;
    List<String> reasons;
    private int count;
    List<String> imageUrl;
}
