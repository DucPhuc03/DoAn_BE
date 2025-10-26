package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    
    private final PostService postService;
    
    @PostMapping
    public ResponseEntity<RestResponse<String>> createPost(@Valid @RequestBody CreatePostDTO createPostDTO) {
            RestResponse<String> response = postService.createPost(createPostDTO);
            return ResponseEntity.ok(response);
    }
}