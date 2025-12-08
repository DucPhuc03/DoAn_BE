package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReportDTO {
    private Long reportedId;
    private String reason;

}
