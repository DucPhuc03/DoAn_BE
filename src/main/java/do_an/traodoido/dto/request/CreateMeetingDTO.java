package do_an.traodoido.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMeetingDTO {
    private Long tradeId;
    private String location;
    private String time;
    private String note;
    private LocalDate date;
}

