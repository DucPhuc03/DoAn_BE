package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreateLikeDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Image;
import do_an.traodoido.entity.Like;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.LikeRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.service.LikeService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    public RestResponse<String> likePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new InvalidException("Post not found"));
        User currentUser = userService.getCurrentUser();

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, currentUser.getId());
        if(existingLike.isPresent()) {
            // Unlike: xóa like và giảm likeCount
            likeRepository.delete(existingLike.get());
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
        }
        else {
            // Like: tạo like mới và tăng likeCount
            Like like = Like.builder()
                    .post(post)
                    .user(currentUser)
                    .build();
            likeRepository.save(like);
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
        }

        return RestResponse.<String>builder()
                .code(1000)
                .message("Success")
                .data("Post liked successfully")
                .build();
    }


}
