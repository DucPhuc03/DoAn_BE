package do_an.traodoido.dto.response;

import do_an.traodoido.enums.MeetingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TradeMeetingDTO {
    private Long meetingId;
    private Long tradeId;
    private String location;
    private LocalDate meetingDate;
    private String time;
    private String note;
    private MeetingStatus status;

    private boolean isCreator;
    private boolean canEdit;
    private boolean canCancel;

}
