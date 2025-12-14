package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResFollowDTO {
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private String username;

}
