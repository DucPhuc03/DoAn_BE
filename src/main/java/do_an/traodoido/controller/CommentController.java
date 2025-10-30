package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateCommentDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    public ResponseEntity<RestResponse<String>> createComment(@Valid @RequestBody CreateCommentDTO createCommentDTO) {
        RestResponse<String> response = commentService.createComment(createCommentDTO);
        return ResponseEntity.ok(response);
    }
}
