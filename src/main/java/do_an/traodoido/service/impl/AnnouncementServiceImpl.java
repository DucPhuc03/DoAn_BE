package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Announcement;
import do_an.traodoido.entity.User;
import do_an.traodoido.repository.AnnouncementRepository;
import do_an.traodoido.service.AnnouncementService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final UserService userService;
    @Override
    public RestResponse<List<Announcement>> getAnnouncementsForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        List<Announcement> announcements = announcementRepository.findAllByUserIdOrderByTimeDesc(currentUser.getId());
        return RestResponse.<List<Announcement>>builder()
                .code(1000)
                .message("Success")
                .data(announcements)
                .build();
    }

    @Override
    public RestResponse<String> updateIsRead(Long id) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        assert announcement != null;
        announcement.setRead(true);
        announcementRepository.save(announcement);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data("Announcement marked as read")
                .build();
    }
}
