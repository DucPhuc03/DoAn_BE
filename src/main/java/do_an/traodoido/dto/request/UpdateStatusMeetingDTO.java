package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStatusMeetingDTO {
    private Long meetingId;
    private String status;
}
