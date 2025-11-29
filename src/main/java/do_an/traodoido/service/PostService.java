package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.request.UpdateStatusPost;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.ResPostDetailDTO;
import do_an.traodoido.dto.response.RestPageResponse;
import do_an.traodoido.dto.response.RestResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    RestResponse<String> createPost(CreatePostDTO createPostDTO, MultipartFile[] images) throws IOException;
    RestResponse<String> deletePost(Long postId, Long userId);
    RestResponse<ResPostDetailDTO> getPostDetails(Long postId);
    RestResponse<List<ResPostDetailDTO>> getPostByUserId(Long userId);
    RestResponse<String> updatePost(CreatePostDTO createPostDTO, MultipartFile[] images, Long postId) throws IOException;

    RestResponse<String> changePostStatus(UpdateStatusPost updateStatusPost);

    List<ResPostDTO> getLikedPostsByUser(Long userId);

    RestPageResponse<List<ResPostDetailDTO>> searchPosts(
            String title,
            String categoryName,
            int page,
            int size
    );
}
