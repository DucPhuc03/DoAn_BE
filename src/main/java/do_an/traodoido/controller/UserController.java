package do_an.traodoido.controller;

import do_an.traodoido.dto.response.ProfileDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
}
