package do_an.traodoido.controller;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
@Tag(name = "Like", description = "API quản lý lượt thích bài đăng")
@SecurityRequirement(name = "Bearer Authentication")
public class LikeController {
    private final LikeService likeService;
    
    @Operation(summary = "Like/Unlike bài đăng", description = "Thích hoặc bỏ thích một bài đăng. Nếu đã like thì sẽ unlike và ngược lại. Yêu cầu xác thực.")
    @PostMapping("/{postId}")
    public ResponseEntity<RestResponse<String>> likePost(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.likePost(postId));
    }
}
