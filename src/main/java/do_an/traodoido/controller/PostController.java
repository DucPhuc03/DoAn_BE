package do_an.traodoido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.JwtService;
import do_an.traodoido.service.PostService;
import do_an.traodoido.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    
    private final PostService postService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> createPost(
            @RequestPart("images") MultipartFile[] images,
            @Valid @RequestPart("postDTO") String postDTOJson
    ) throws IOException {
        CreatePostDTO createPostDTO = objectMapper.readValue(postDTOJson, CreatePostDTO.class);
        RestResponse<String> response = postService.createPost(createPostDTO, images);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ResPostDTO>> getPostDetails(@PathVariable Long id) {

        log.info("Received request to get all categories"+userService.getCurrentUserId());
        RestResponse<ResPostDTO> response = postService.getPostDetails(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse<List<ResPostDTO>>> getPostsByUserId(@PathVariable Long userId) {
        RestResponse<List<ResPostDTO>> response = postService.getPostByUserId(userId);
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

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> updatePost(
            @PathVariable Long id,
            @RequestPart("postDTO") String postDTOJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws IOException {
        CreatePostDTO updateDTO = objectMapper.readValue(postDTOJson, CreatePostDTO.class);
        RestResponse<String> response = postService.updatePost(updateDTO, images, id);
        return ResponseEntity.ok(response);
    }
}