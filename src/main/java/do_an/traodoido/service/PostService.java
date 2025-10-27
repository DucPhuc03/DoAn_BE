package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface PostService {
    RestResponse<String> createPost(CreatePostDTO createPostDTO);
    RestResponse<String> deletePost(Long postId, Long userId);
    RestResponse<ResPostDTO> getPostDetails(Long postId);
    RestResponse<List<ResPostDTO>> getAllPosts(Long userId);
}
