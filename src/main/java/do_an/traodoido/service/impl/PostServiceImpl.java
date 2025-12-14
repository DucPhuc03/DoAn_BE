package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.request.UpdateStatusPost;
import do_an.traodoido.dto.response.*;
import do_an.traodoido.entity.*;
import do_an.traodoido.enums.PostStatus;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.exception.UnauthorizedAccessException;
import do_an.traodoido.repository.*;
import do_an.traodoido.service.PostService;
import do_an.traodoido.util.EmbeddingService;
import do_an.traodoido.util.GeocodingService;
import do_an.traodoido.util.S3Service;

import do_an.traodoido.util.VisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final LikeRepository likeRepository;
    private final VisionService visionService;
    private final GeocodingService geocodingService;
    private final EmbeddingService embeddingService;
    
    @Override
    public RestResponse<String> createPost(CreatePostDTO createPostDTO, MultipartFile[] images) throws IOException {
            Category category = categoryRepository.findById(createPostDTO.getCategoryId())
                    .orElseThrow(() -> new InvalidException("Category", createPostDTO.getCategoryId()));

            User user = userRepository.findById(createPostDTO.getUserId())
                    .orElseThrow(() -> new InvalidException("User", createPostDTO.getUserId()));
            // Tạo post mới
            Post post = Post.builder()
                    .title(createPostDTO.getTitle())
                    .description(createPostDTO.getDescription())
                    .tag(createPostDTO.getTag())
                    .itemCondition(createPostDTO.getItemCondition())
                    .postDate(createPostDTO.getPostDate() != null ? createPostDTO.getPostDate() : LocalDate.now())
                    .tradeLocation(createPostDTO.getTradeLocation())
                    .postStatus(PostStatus.WAITING)
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

            String predictedCategory = visionService.detectLabels(images[0].getBytes());
            if(predictedCategory.equals(category.getName())) {
                post.setPostStatus(PostStatus.AVAILABLE);
                postRepository.save(post);
            }
            return RestResponse.<String>builder()
                    .code(1000)
                    .message("Post created successfully")
                    .data("Post created with id: " + post.getId())
                    .build();
    }

    @Override
    public RestResponse<String> deletePost(Long postId) {
        User currentUser = resolveCurrentUser();
        // Tìm post theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new InvalidException("Post", postId));

        // Kiểm tra quyền xóa - chỉ owner mới được xóa
        if ( currentUser.getRole().equals("ADMIN")) {
            postRepository.delete(post);
        }
        else if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to delete this post");
        }
        // Xóa post (cascade sẽ xóa luôn images)
        post.setPostStatus(PostStatus.DELETED);
        postRepository.save(post);
        
        return RestResponse.<String>builder()
                .code(1000)
                .message("Post deleted successfully")
                .data("Post with id " + postId + " has been deleted")
                .build();
    }

    @Override
    public RestResponse<ResPostDetailDTO> getPostDetails(Long postId) {
        // Tìm post theo ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new InvalidException("Post", postId));

        User user = resolveCurrentUser();

        boolean isLiked = likeRepository.existsByPostIdAndUserId(postId, user.getId());
        boolean isOwner = post.getUser().getId().equals(user.getId());
        // Lấy danh sách image URLs
        List<String> imageUrls = post.getImages() != null 
                ? post.getImages().stream()
                    .map(Image::getImageUrl)
                    .collect(Collectors.toList())
                : List.of();

        List<CommentDTO> commentDTOs = post.getComments() != null
                ? post.getComments().stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .userId(comment.getUser().getId())
                        .avatarUrl(comment.getUser().getAvatarUrl())
                        .fullName(comment.getUser().getUsername())
                        .content(comment.getContent())
                        .commentDate(comment.getCommentDate())
                        .build())
                .toList()
                : List.of();
        // Tạo ResPostDTO
        ResPostDetailDTO resPostDTO = ResPostDetailDTO.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .avatarUrl(post.getUser().getAvatarUrl())
                .title(post.getTitle())
                .description(post.getDescription())
                .tag(post.getTag())
                .itemCondition(post.getItemCondition())
                .postDate(post.getPostDate())
                .tradeLocation(post.getTradeLocation())
                .postStatus(post.getPostStatus())
                .imageUrls(imageUrls)
                .comments(commentDTOs)
                .totalComments(commentDTOs.size())
                .totalLikes(post.getLikeCount())
                .category(post.getCategory())
                .isOwner(isOwner)
                .canEdit(isOwner)
                .canDelete(isOwner)
                .canReport(!isOwner)
                .canUpdateStatus(isOwner)
                .isLiked(isLiked)
                .build();
        
        return RestResponse.<ResPostDetailDTO>builder()
                .code(1000)
                .message("Post details retrieved successfully")
                .data(resPostDTO)
                .build();
    }

    @Override
    public RestResponse<List<ResPostDTO>> getPostByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserIdAndPostStatusNotIn(userId, List.of(PostStatus.DELETED, PostStatus.COMPLETED));
        List<ResPostDTO> resPostDTOs = posts.stream().map(post -> {
            return ResPostDTO.builder()
                   .id(post.getId())
                   .username(post.getUser().getUsername())
                   .title(post.getTitle())
                   .postDate(post.getPostDate())
                    .postStatus(post.getPostStatus())
                   .imageUrl(post.getImages().stream().findFirst().map(Image::getImageUrl).orElse(null))
                   .category(post.getCategory())
                   .build();
        }).toList();
        return RestResponse.<List<ResPostDTO>>builder()
                .code(1000)
                .message("Posts retrieved successfully")
                .data(resPostDTOs)
                .build();
    }

    @Override
    public RestResponse<String> updatePost(CreatePostDTO createPostDTO, MultipartFile[] images, Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new InvalidException("Post", postId));
        User user = resolveCurrentUser();

        if (!post.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to update this post");
        }
        Category category = categoryRepository.findById(createPostDTO.getCategoryId())
                .orElseThrow(() -> new InvalidException("Category", createPostDTO.getCategoryId()));

        post.setTitle(createPostDTO.getTitle());
        post.setDescription(createPostDTO.getDescription());
        post.setTag(createPostDTO.getTag());
        post.setItemCondition(createPostDTO.getItemCondition());
        post.setPostDate(createPostDTO.getPostDate() != null ? createPostDTO.getPostDate() : post.getPostDate());
        post.setTradeLocation(createPostDTO.getTradeLocation());
            post.setLatitude(createPostDTO.getLatitude());
            post.setLongitude(createPostDTO.getLongitude());

        post.setCategory(category);

        postRepository.save(post);

        // If new images provided, replace existing images
        if (images != null && images.length > 0) {
            List<Image> existingImages = post.getImages();
            if (existingImages != null && !existingImages.isEmpty()) {
                imageRepository.deleteAll(existingImages);
            }
            List<String> imageUrls = s3Service.uploadFiles(List.of(images), "post");
            List<Image> imageEntities = imageUrls.stream()
                    .map(url -> Image.builder()
                            .imageUrl(url)
                            .post(post)
                            .build())
                    .toList();
            imageRepository.saveAll(imageEntities);
        }

        return RestResponse.<String>builder()
                .code(1000)
                .message("Post updated successfully")
                .data("Post updated with id: " + post.getId())
                .build();
    }

    @Override
    public RestResponse<String> changePostStatus(UpdateStatusPost updateStatusPost) {
        Post post = postRepository.findById(updateStatusPost.getPostId()).orElseThrow(() -> new InvalidException("Post", updateStatusPost.getPostId()));
        post.setPostStatus(Enum.valueOf(PostStatus.class, updateStatusPost.getStatus()));
        postRepository.save(post);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Post status updated successfully")
                .data("Post status updated to: " + updateStatusPost.getStatus())
                .build();
    }

    @Override
    public List<ResPostDTO> getLikedPostsByUser(Long userId) {

        List<Like> likes = likeRepository.findByUserId(userId);

        List<ResPostDTO> likedPosts = likes.stream()
                .map(like -> {
                    Post post = like.getPost();
                    return ResPostDTO.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .username(post.getUser().getUsername())
                            .postDate(post.getPostDate())
                            .imageUrl(post.getImages().stream().findFirst().map(Image::getImageUrl).orElse(null))
                            .totalLikes(post.getLikeCount())
                            .category(post.getCategory())

                            .build();
                })
                .toList();
        return likedPosts;
    }

    @Override
    public RestResponse<List<ResPostDTO>> getPostsByAdmin() {
        List<Post> posts = postRepository.findAllByPostStatus(PostStatus.WAITING);
        List<ResPostDTO> resPostDTOs = posts.stream()
                .map(this::mapToResPostDTO)
                .toList();

        return RestResponse.<List<ResPostDTO>>builder()
                .code(1000)
                .message("Posts retrieved successfully")
                .data(resPostDTOs)
                .build();
    }

    @Override
    public RestPageResponse<List<ResPostDTO>> searchPosts(String title, String categoryName,int maxDistance, int page, int size) {
        User currentUser = resolveCurrentUser();
        Pageable pageable = PageRequest.of(page-1, size);
        String normalizedTitle = normalizeQueryParam(title);
        String normalizedCategoryName = normalizeQueryParam(categoryName);
        Page<Post> postPage;
        if (normalizedTitle == null && normalizedCategoryName == null) {
            postPage = postRepository.findByPostStatusNotIn(List.of(PostStatus.WAITING, PostStatus.COMPLETED,PostStatus.DELETED),pageable);
        } else {
            postPage = postRepository.searchPostsByTitleAndCategory(
                    normalizedTitle,
                    normalizedCategoryName,
                    List.of(PostStatus.WAITING, PostStatus.COMPLETED,PostStatus.DELETED),
                    pageable
            );
        }
        List<ResPostDTO> resPostDTOs = postPage.getContent().stream()
                .map(post -> {
                    double distance = geocodingService.calculateDistance(
                            currentUser.getLatitude(),
                            currentUser.getLongitude(),
                            post.getLatitude(),
                            post.getLongitude()
                    );

                    return ResPostDTO.builder()
                            .id(post.getId())
                            .username(post.getUser().getFullName())
                            .title(post.getTitle())
                            .postStatus(post.getPostStatus())
                            .totalLikes(post.getLikeCount())
                            .postDate(post.getPostDate())
                            .imageUrl(post.getImages().stream()
                                    .findFirst()
                                    .map(Image::getImageUrl)
                                    .orElse(null))
                            .category(post.getCategory())
                            .distance(distance)              // 👈 thêm vào DTO
                            .build();
                })
                .filter(resPostDTO -> resPostDTO.getDistance() <maxDistance) // Lọc các bài đăng có khoảng cách hợp lệ
                .sorted((java.util.Comparator.comparingDouble(ResPostDTO::getDistance)) // Sắp xếp theo khoảng cách
                )
                .toList();


        MetaData metaData = MetaData.builder()
                .page(postPage.getNumber()+1)
                .size(postPage.getSize())
                .totalElements((int) postPage.getTotalElements())
                .totalPages(postPage.getTotalPages())
                .build();

        return RestPageResponse.<List<ResPostDTO>>builder()
                .code(1000)
                .message("Posts retrieved successfully")
                .data(resPostDTOs)
                .metaData(metaData)
                .build();
    }

    @Override
    public RestResponse<List<ResPostDTO>> recommendForUser() {
        User user=resolveCurrentUser();
        List<RecommendedItem> recommendedItems=embeddingService.recommendForUser(user.getId());
        List<Post> posts=recommendedItems.stream().map(item->postRepository.findById(item.getId()).orElseThrow()).toList();
        List<ResPostDTO> resPostDTOs = posts.stream()
                .map(post -> {
                    double distance = geocodingService.calculateDistance(
                            user.getLatitude(),
                            user.getLongitude(),
                            post.getLatitude(),
                            post.getLongitude()
                    );
                    return ResPostDTO.builder()
                            .id(post.getId())
                            .username(post.getUser().getFullName())
                            .title(post.getTitle())
                            .postStatus(post.getPostStatus())
                            .totalLikes(post.getLikeCount())
                            .postDate(post.getPostDate())
                            .imageUrl(post.getImages().stream()
                                    .findFirst()
                                    .map(Image::getImageUrl)
                                    .orElse(null))
                            .category(post.getCategory())
                            .distance(distance)
                            .build();
                })
                .toList();
        return RestResponse.<List<ResPostDTO>>builder()
                .code(1000)
                .message("Posts recommended successfully")
                .data(resPostDTOs)
                .build();
    }

    private String normalizeQueryParam(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private ResPostDTO mapToResPostDTO(Post post) {
        return ResPostDTO.builder()
                .id(post.getId())
                .username(post.getUser().getFullName())
                .title(post.getTitle())
                .postStatus(post.getPostStatus())
                .totalLikes(post.getLikeCount())
                .postDate(post.getPostDate())
                .imageUrl(post.getImages().stream().findFirst().map(Image::getImageUrl).orElse(null))
                .category(post.getCategory())
                .build();
    }

    private User resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidException("User", "username", username);
        }
        return user;
    }
}
