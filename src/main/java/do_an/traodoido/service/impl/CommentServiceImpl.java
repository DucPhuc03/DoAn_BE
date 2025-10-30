package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateCommentDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Comment;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.CommentRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private  final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    @Override
    public RestResponse<String> createComment(CreateCommentDTO createCommentDTO) {
        Post post = postRepository.findById(createCommentDTO.getPostId()).orElseThrow(()->new InvalidException("Post not found with id: " + createCommentDTO.getPostId()));
        User user = userRepository.findById(createCommentDTO.getUserId()).orElseThrow(()->new InvalidException("User not found with id: " + createCommentDTO.getUserId()));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(createCommentDTO.getContent())
                .commentDate(createCommentDTO.getCommentDate())
                .build();
        commentRepository.save(comment);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data("Comment created successfully")
                .build();
    }

    @Override
    public RestResponse<String> getCommentsByPostId(Long postId) {
        return null;
    }

    @Override
    public RestResponse<String> deleteComment(Long commentId) {
        return null;
    }
}
