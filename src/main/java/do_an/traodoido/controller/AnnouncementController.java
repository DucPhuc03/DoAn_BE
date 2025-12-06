package do_an.traodoido.controller;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Announcement;
import do_an.traodoido.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public ResponseEntity<RestResponse<List<Announcement>>> getAnnouncementsForCurrentUser() {
        RestResponse<List<Announcement>> response = announcementService.getAnnouncementsForCurrentUser();
        return ResponseEntity.ok(response);
    }
}
