package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Category;
import do_an.traodoido.entity.Image;
import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.ResourceNotFoundException;
import do_an.traodoido.exception.UnauthorizedAccessException;
import do_an.traodoido.repository.CategoryRepository;
import do_an.traodoido.repository.ImageRepository;
import do_an.traodoido.repository.PostRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.PostService;
import do_an.traodoido.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    
    @Override
    public RestResponse<String> createPost(CreatePostDTO createPostDTO, MultipartFile[] images) throws IOException {

            Category category = categoryRepository.findById(createPostDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", createPostDTO.getCategoryId()));

            User user = userRepository.findById(createPostDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", createPostDTO.getUserId()));
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
            List<String> imageUrls = s3Service.uploadFiles(List.of(images),"post");
            List<Image> imageEntities = imageUrls.stream()
                    .map(url -> Image.builder()
                            .imageUrl(url)
                            .post(post)
                            .build())
                    .toList();
            imageRepository.saveAll(imageEntities);
            return RestResponse.<String>builder()
                    .code(1000)
                    .message("Post created successfully")
                    .data("Post created with id: " + post.getId())
                    .build();
    }

    @Override
    public RestResponse<String> deletePost(Long postId, Long userId) {
        // Tìm post theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        
        // Kiểm tra quyền xóa - chỉ owner mới được xóa
        if (!post.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You don't have permission to delete this post");
        }
        
        // Xóa post (cascade sẽ xóa luôn images)
        postRepository.delete(post);
        
        return RestResponse.<String>builder()
                .code(1000)
                .message("Post deleted successfully")
                .data("Post with id " + postId + " has been deleted")
                .build();
    }

    @Override
    public RestResponse<ResPostDTO> getPostDetails(Long postId) {
        // Tìm post theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        
        // Lấy danh sách image URLs
        List<String> imageUrls = post.getImages() != null 
                ? post.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList())
                : List.of();
        
        // Tạo ResPostDTO
        ResPostDTO resPostDTO = ResPostDTO.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .title(post.getTitle())
                .description(post.getDescription())
                .itemCondition(post.getItemCondition())
                .postDate(post.getPostDate())
                .tradeLocation(post.getTradeLocation())
                .postStatus(post.getPostStatus())
                .imageUrls(imageUrls)
                .categoryName(post.getCategory().getName())
                .categoryId(post.getCategory().getId())
                .build();
        
        return RestResponse.<ResPostDTO>builder()
                .code(1000)
                .message("Post details retrieved successfully")
                .data(resPostDTO)
                .build();
    }

    @Override
    public RestResponse<List<ResPostDTO>> getAllPosts(Long userId) {
        return null;
    }
}
