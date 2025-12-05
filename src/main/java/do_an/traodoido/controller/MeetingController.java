package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateMeetingDTO;
import do_an.traodoido.dto.response.ResMeetingDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
@RequiredArgsConstructor
@Tag(name = "Meeting", description = "API quản lý cuộc hẹn trao đổi đồ")
@SecurityRequirement(name = "Bearer Authentication")
public class MeetingController {
    private final MeetingService meetingService;

    @Operation(summary = "Tạo cuộc hẹn mới", description = "Tạo một cuộc hẹn để trao đổi đồ giữa hai người dùng. Yêu cầu xác thực.")
    @PostMapping
    public ResponseEntity<RestResponse<String>> createMeeting(@RequestBody CreateMeetingDTO request) {
        return ResponseEntity.ok(meetingService.createMeeting(request));
    }

    @Operation(summary = "Lấy danh sách cuộc hẹn của người dùng", description = "Lấy tất cả cuộc hẹn của người dùng hiện tại. Yêu cầu xác thực.")
    @GetMapping
    public ResponseEntity<RestResponse<List<ResMeetingDTO>>> getMeetingByTradeId() {
        return ResponseEntity.ok(meetingService.getMeetingsByUser());
    }

    @Operation(summary = "Cập nhật trạng thái cuộc hẹn", description = "Cập nhật trạng thái của cuộc hẹn (ví dụ: CONFIRMED, COMPLETED, CANCELLED). Yêu cầu xác thực.")
    @PatchMapping("/{meetingId}")
    public ResponseEntity<RestResponse<String>> updateStatusMeeting(@PathVariable Long meetingId) {
        return ResponseEntity.ok(meetingService.updateStatusMeeting(meetingId));
    }
    
    @Operation(summary = "Hủy cuộc hẹn", description = "Hủy một cuộc hẹn trao đổi đồ. Yêu cầu xác thực.")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<RestResponse<String>> cancelMeeting(@PathVariable Long meetingId) {
        return ResponseEntity.ok(meetingService.cancelMeeting(meetingId));
    }
}
