package do_an.traodoido.service;

import do_an.traodoido.dto.response.ProfileDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User getCurrentUser();
    String getCurrentUsername();
    Long getCurrentUserId();

    RestResponse<ProfileDTO> getProfile(Long userId);

    RestResponse<String> updateAvatar(MultipartFile avatarFile) throws IOException;
}


