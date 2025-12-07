package do_an.traodoido.controller;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Announcement;
import do_an.traodoido.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;
    @GetMapping
    public ResponseEntity<RestResponse<List<Announcement>>> getAnnouncementsForCurrentUser() {
        RestResponse<List<Announcement>> response = announcementService.getAnnouncementsForCurrentUser();
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<RestResponse<String>> updateIsRead(@PathVariable Long id) {
        RestResponse<String> response = announcementService.updateIsRead(id);
        return ResponseEntity.ok(response);
    }
}
