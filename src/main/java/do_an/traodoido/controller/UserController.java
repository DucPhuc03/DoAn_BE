package do_an.traodoido.controller;

import do_an.traodoido.dto.request.UpdateProfileDTO;
import do_an.traodoido.dto.response.ProfileDTO;
import do_an.traodoido.dto.response.ResUserDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "API quản lý thông tin người dùng")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;
    @Operation(summary = "Lấy thông tin profile người dùng", description = "Lấy thông tin chi tiết profile của một người dùng theo ID. Yêu cầu xác thực.")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<RestResponse<ProfileDTO>> getUserProfile(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @Operation(summary = "Cập nhật avatar", description = "Cập nhật ảnh đại diện của người dùng hiện tại. Yêu cầu xác thực.")
    @PatchMapping("/avatar")
    public ResponseEntity<RestResponse<String>> updateAvatar(@RequestParam("avatar") MultipartFile avatarFile) throws Exception {
        return ResponseEntity.ok(userService.updateAvatar(avatarFile));
    }
    @Operation(summary = "Cập nhật thông tin profile", description = "Cập nhật thông tin cá nhân của người dùng hiện tại (tên, mô tả, v.v.). Yêu cầu xác thực.")
    @PatchMapping("/profile")
    public ResponseEntity<RestResponse<String>> updateProfile(@RequestBody UpdateProfileDTO updateProfileDTO) {
        return ResponseEntity.ok(userService.updateProfile(updateProfileDTO));
    }

    @Operation(summary = "Lấy danh sách tất cả người dùng (Admin)", description = "Lấy danh sách tất cả người dùng trong hệ thống. Chỉ dành cho Admin.")
    @GetMapping("/admin")
    public ResponseEntity<RestResponse<List<ResUserDTO>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Cập nhật trạng thái người dùng (Admin)", description = "Thay đổi trạng thái của người dùng (ví dụ: ACTIVE, INACTIVE, BANNED). Chỉ dành cho Admin.")
    @PatchMapping("/admin/status/{userId}")
    public ResponseEntity<RestResponse<String>> updateUserStatus(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.updateStatus(userId));
    }
}
