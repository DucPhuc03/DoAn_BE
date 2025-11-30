package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateMeetingDTO;
import do_an.traodoido.dto.request.UpdateStatusMeetingDTO;
import do_an.traodoido.dto.response.ResMeetingDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.TradeMeetingDTO;

import java.util.List;

public interface MeetingService {
    RestResponse<String> createMeeting(CreateMeetingDTO request);
    RestResponse<List<ResMeetingDTO>> getMeetingsByUser();

    TradeMeetingDTO  getMeetingTrade(Long tradeId);

   RestResponse<String> updateStatusMeeting(Long meetingId);

    RestResponse<String> cancelMeeting(Long meetingId);


}
