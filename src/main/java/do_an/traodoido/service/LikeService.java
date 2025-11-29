package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreateLikeDTO;
import do_an.traodoido.dto.response.ResPostDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface LikeService {
    RestResponse<String> likePost(Long postId);



}
