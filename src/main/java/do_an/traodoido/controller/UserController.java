package do_an.traodoido.controller;

import do_an.traodoido.dto.request.UpdateProfileDTO;
import do_an.traodoido.dto.response.ProfileDTO;
import do_an.traodoido.dto.response.ResUserDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    @GetMapping("/profile/{userId}")
    public ResponseEntity<RestResponse<ProfileDTO>> getUserProfile(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PatchMapping("/avatar")
    public ResponseEntity<RestResponse<String>> updateAvatar(
            @RequestParam("avatar") MultipartFile avatarFile) throws Exception {
        return ResponseEntity.ok(userService.updateAvatar(avatarFile));
    }
    @PatchMapping("/profile")
    public ResponseEntity<RestResponse<String>> updateProfile(
            @RequestBody UpdateProfileDTO updateProfileDTO) {
        return ResponseEntity.ok(userService.updateProfile(updateProfileDTO));
    }

    @GetMapping("/admin")
    public ResponseEntity<RestResponse<List<ResUserDTO>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/admin/status/{userId}")
    public ResponseEntity<RestResponse<String>> updateUserStatus(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.updateStatus(userId));
    }
}
