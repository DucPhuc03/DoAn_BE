package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateMeetingDTO;
import do_an.traodoido.dto.response.ResMeetingDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface MeetingService {
    RestResponse<String> createMeeting(CreateMeetingDTO request);
    RestResponse<List<ResMeetingDTO>> getMeetingsByUser();
}
