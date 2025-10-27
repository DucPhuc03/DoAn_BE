package do_an.traodoido.dto.response;

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
    private String email;
    private String role;
}
