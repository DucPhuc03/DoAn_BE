package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateCommentDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Announcement;
import do_an.traodoido.entity.Comment;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.AnnouncementRepository;
import do_an.traodoido.repository.CommentRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.CommentService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private  final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final AnnouncementRepository announcementRepository;
    @Override
    public RestResponse<String> createComment(CreateCommentDTO createCommentDTO) {
        Post post = postRepository.findById(createCommentDTO.getPostId()).orElseThrow(()->new InvalidException("Post not found with id: " + createCommentDTO.getPostId()));
        User user = userService.getCurrentUser();

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(createCommentDTO.getContent())
                .commentDate(LocalDate.now())
                .build();
        commentRepository.save(comment);
        announcementRepository.save(Announcement.builder()
                .user(post.getUser())
                .title("Bình luận mới")
                .type("comment")
                .message(user.getFullName() + " đã bình luận bài đăng của bạn.")
                .time(LocalDate.now())
                .isRead(false)
                .link("/post/" + post.getId())
                .build());
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
