package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.request.CreateReviewDTO;
import do_an.traodoido.dto.response.ResReviewDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface ReviewService {

    RestResponse<String> createReview(CreateReviewDTO createReviewDTO);
    RestResponse<List<ResReviewDTO>> getReviewUser(Long userId);

}
