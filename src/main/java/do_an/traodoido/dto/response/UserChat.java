package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChat {
    private Long userId;
    private String username;
    private String avatarUrl;
}
