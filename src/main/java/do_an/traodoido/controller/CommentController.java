package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateCommentDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "API quản lý bình luận trên bài đăng")
@SecurityRequirement(name = "Bearer Authentication")
public class CommentController {
    private final CommentService commentService;
    
    @Operation(summary = "Tạo bình luận mới", description = "Thêm bình luận vào một bài đăng. Yêu cầu xác thực.")
    @PostMapping
    public ResponseEntity<RestResponse<String>> createComment(@Valid @RequestBody CreateCommentDTO createCommentDTO) {
        RestResponse<String> response = commentService.createComment(createCommentDTO);
        return ResponseEntity.ok(response);
    }
}
