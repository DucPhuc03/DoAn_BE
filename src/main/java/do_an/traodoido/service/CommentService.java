package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateCommentDTO;
import do_an.traodoido.dto.response.RestResponse;

public interface CommentService {
    RestResponse<String> createComment(CreateCommentDTO createCommentDTO);
    RestResponse<String> getCommentsByPostId(Long postId);
    RestResponse<String> deleteComment(Long commentId);
}
