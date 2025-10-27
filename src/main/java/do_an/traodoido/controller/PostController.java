package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.JwtService;
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
    private final JwtService jwtService;
    
    @PostMapping
    public ResponseEntity<RestResponse<String>> createPost(@Valid @RequestBody CreatePostDTO createPostDTO) {
        RestResponse<String> response = postService.createPost(createPostDTO);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ResPostDTO>> getPostDetails(@PathVariable Long id) {
        RestResponse<ResPostDTO> response = postService.getPostDetails(id);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<String>> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader) {
        
        // Extract token from "Bearer <token>"
        String token = authorizationHeader.substring(7);
        
        // Extract user ID from token
        Long userId = jwtService.extractUserId(token);
        if (userId == null) {
            throw new RuntimeException("Invalid token");
        }
        
        RestResponse<String> response = postService.deletePost(id, userId);
        return ResponseEntity.ok(response);
    }
}