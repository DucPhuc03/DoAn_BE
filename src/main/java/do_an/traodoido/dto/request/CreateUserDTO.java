package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDTO {
    private String username;
    private String password;
    private String email;
}
