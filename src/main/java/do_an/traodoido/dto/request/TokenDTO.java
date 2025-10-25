package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

}
