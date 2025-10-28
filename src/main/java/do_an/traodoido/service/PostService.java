package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    RestResponse<String> createPost(CreatePostDTO createPostDTO, MultipartFile[] images) throws IOException;
    RestResponse<String> deletePost(Long postId, Long userId);
    RestResponse<ResPostDTO> getPostDetails(Long postId);
    RestResponse<List<ResPostDTO>> getAllPosts(Long userId);
}
