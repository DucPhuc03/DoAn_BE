package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MeetingConversation {
    private  String location;
    private String time;
    private LocalDate date;
}
