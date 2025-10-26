package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.CategoryNotFoundException;
import do_an.traodoido.exception.UserNotFoundException;
import do_an.traodoido.repository.CategoryRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    
    @Override
    public RestResponse<String> createPost(CreatePostDTO createPostDTO) {
            // Kiểm tra category có tồn tại không
            Category category = categoryRepository.findById(createPostDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + createPostDTO.getCategoryId()));
            // Kiểm tra user có tồn tại không
            User user = userRepository.findById(createPostDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + createPostDTO.getUserId()));
            // Tạo post mới
            Post post = Post.builder()
                    .title(createPostDTO.getTitle())
                    .description(createPostDTO.getDescription())
                    .itemCondition(createPostDTO.getItemCondition())
                    .postDate(createPostDTO.getPostDate() != null ? createPostDTO.getPostDate() : LocalDate.now())
                    .tradeLocation(createPostDTO.getTradeLocation())
                    .category(category)
                    .user(user)
                    .build();
            postRepository.save(post);
            return RestResponse.<String>builder()
                    .code(1000)
                    .message("Post created successfully")
                    .data("Post created with id: " + post.getId())
                    .build();
    }

    @Override
    public RestResponse<String> deletePost(Long postId) {
        return null;
    }

    @Override
    public RestResponse<ResPostDTO> getPostDetails(Long postId) {
        return null;
    }

    @Override
    public RestResponse<List<ResPostDTO>> getAllPosts(Long userId) {
        return null;
    }
}
