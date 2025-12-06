package do_an.traodoido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.request.UpdateStatusPost;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.ResPostDetailDTO;
import do_an.traodoido.dto.response.RestPageResponse;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.util.JwtService;
import do_an.traodoido.service.PostService;
import do_an.traodoido.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Post", description = "API quản lý bài đăng trao đổi đồ")
@SecurityRequirement(name = "Bearer Authentication")
public class PostController {
    
    private final PostService postService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Operation(summary = "Tạo bài đăng mới", description = "Tạo một bài đăng trao đổi đồ mới với hình ảnh. Yêu cầu xác thực.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<String>> createPost(
            @RequestPart("images") MultipartFile[] images,
            @Valid @RequestPart("postDTO") String postDTOJson
    ) throws IOException {
        CreatePostDTO createPostDTO = objectMapper.readValue(postDTOJson, CreatePostDTO.class);
        RestResponse<String> response = postService.createPost(createPostDTO, images);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Lấy chi tiết bài đăng", description = "Lấy thông tin chi tiết của một bài đăng theo ID. Yêu cầu xác thực.")
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ResPostDetailDTO>> getPostDetails(@PathVariable Long id) {
        RestResponse<ResPostDetailDTO> response = postService.getPostDetails(id);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Lấy danh sách bài đăng theo người dùng", description = "Lấy tất cả bài đăng của một người dùng cụ thể. Yêu cầu xác thực.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse<List<ResPostDTO>>> getPostsByUserId(@PathVariable Long userId) {
        RestResponse<List<ResPostDTO>> response = postService.getPostByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy tất cả bài đăng (Admin)", description = "Lấy danh sách tất cả bài đăng trong hệ thống. Chỉ dành cho Admin.")
    @GetMapping("/admin")
    public ResponseEntity<RestResponse<List<ResPostDTO>>> getPostsByAdmin() {
        RestResponse<List<ResPostDTO>> response = postService.getPostsByAdmin();
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Xóa bài đăng", description = "Xóa một bài đăng theo ID. Chỉ chủ sở hữu bài đăng mới có thể xóa. Yêu cầu xác thực.")
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<String>> deletePost(@PathVariable Long id) {
        


        RestResponse<String> response = postService.deletePost(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cập nhật bài đăng", description = "Cập nhật thông tin bài đăng. Hình ảnh là tùy chọn. Chỉ chủ sở hữu mới có thể cập nhật. Yêu cầu xác thực.")
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

    @Operation(summary = "Thay đổi trạng thái bài đăng", description = "Thay đổi trạng thái của bài đăng (ví dụ: ACTIVE, INACTIVE). Yêu cầu xác thực.")
    @PostMapping("/status")
    public ResponseEntity<RestResponse<String>> changePostStatus(@RequestBody UpdateStatusPost updateStatusPost) {
        RestResponse<String> response = postService.changePostStatus(updateStatusPost);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Tìm kiếm bài đăng", description = "Tìm kiếm bài đăng theo tiêu đề và/hoặc tên danh mục. Hỗ trợ phân trang. Yêu cầu xác thực.")
    @GetMapping("/search")
    public ResponseEntity<RestPageResponse<List<ResPostDTO>>> searchPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        RestPageResponse<List<ResPostDTO>> response = postService.searchPosts(title, categoryName, page, size);
        return ResponseEntity.ok(response);
    }
}