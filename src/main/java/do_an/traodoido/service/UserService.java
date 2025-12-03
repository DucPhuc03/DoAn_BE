package do_an.traodoido.service;

import do_an.traodoido.dto.request.UpdateProfileDTO;
import do_an.traodoido.dto.response.ProfileDTO;
import do_an.traodoido.dto.response.ResUserDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getCurrentUser();
    String getCurrentUsername();
    Long getCurrentUserId();

    RestResponse<ProfileDTO> getProfile(Long userId);

    RestResponse<String> updateAvatar(MultipartFile avatarFile) throws IOException;

    RestResponse<String> updateProfile(UpdateProfileDTO updateProfileDTO);

    RestResponse<String> updateStatus(Long userId);
    RestResponse<List<ResUserDTO>> getAllUsers();
}


