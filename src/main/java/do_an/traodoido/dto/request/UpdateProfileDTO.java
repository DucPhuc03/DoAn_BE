package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProfileDTO {
    private String fullName;
    private String bio;
    private String phoneNumber;
    private String address;
    private double latitude;
    private double longitude;
}
