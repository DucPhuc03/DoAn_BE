package do_an.traodoido.controller;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @PostMapping("/{postId}")
    public ResponseEntity<RestResponse<String>> likePost(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.likePost(postId));
    }
}
