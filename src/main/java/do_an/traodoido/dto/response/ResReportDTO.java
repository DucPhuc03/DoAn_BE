package do_an.traodoido.dto.response;

import do_an.traodoido.enums.ReportStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ResReportDTO {
    private Long id;
    private Long reportedId;
    private String reason;
    private UserChat reporter;
    private UserChat reportedUser;
    private LocalDate reportDate;
    private ReportStatus status;
}
