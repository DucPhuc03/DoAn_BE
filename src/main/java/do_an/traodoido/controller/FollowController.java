package do_an.traodoido.controller;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{userIdToFollow}")
    public ResponseEntity<RestResponse<String>> followUser(@PathVariable("userIdToFollow") Long userIdToFollow) {
        RestResponse<String> response = followService.followUser(userIdToFollow);
        return ResponseEntity.ok(response);
    }
}
