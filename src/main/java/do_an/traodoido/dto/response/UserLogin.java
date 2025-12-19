package do_an.traodoido.dto.response;

import do_an.traodoido.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class UserLogin {
    private Long id;
    private String name;
    private String avatarUrl;
    private String email;
    private UserStatus status;
    private String role;
}
