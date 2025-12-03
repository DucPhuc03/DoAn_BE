package do_an.traodoido.dto.response;

import do_an.traodoido.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ResUserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String phoneNumber;
    private LocalDate createdAt;
    private String role;
    private UserStatus status;
}
