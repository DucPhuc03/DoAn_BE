package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResMeetingDTO {
    private Long id;
    private String location;
    private LocalDate meetingDate;
    private String titleTrade;
    private String namePartner;
    private String avatarPartner;
}

