package do_an.traodoido.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateAnnouncement {
    private String title;
    private String message;
    private String type;
    private LocalDate time;
    private String link;
    private Long userId;
}
