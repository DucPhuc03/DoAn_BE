package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateMeetingDTO;
import do_an.traodoido.dto.response.ResMeetingDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<RestResponse<String>> createMeeting(@RequestBody CreateMeetingDTO request) {
        return ResponseEntity.ok(meetingService.createMeeting(request));
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<ResMeetingDTO>>> getMeetingByTradeId() {
        return ResponseEntity.ok(meetingService.getMeetingsByUser());
    }

    @PatchMapping("/{meetingId}")
    public ResponseEntity<RestResponse<String>> updateStatusMeeting(@PathVariable Long meetingId) {
        return ResponseEntity.ok(meetingService.updateStatusMeeting(meetingId));
    }
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<RestResponse<String>> cancelMeeting(@PathVariable Long meetingId) {
        return ResponseEntity.ok(meetingService.cancelMeeting(meetingId));
    }
}
