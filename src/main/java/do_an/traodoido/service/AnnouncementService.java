package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateAnnouncement;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
    RestResponse<List<Announcement>> getAnnouncementsForCurrentUser();
    RestResponse<String> updateIsRead(Long id);
    RestResponse<String> createAnnouncement(CreateAnnouncement createAnnouncement);
}
