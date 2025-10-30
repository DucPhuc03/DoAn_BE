package do_an.traodoido.service;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.request.CreateReviewDTO;
import do_an.traodoido.dto.response.RestResponse;

public interface ReviewService {

    RestResponse<String> createReview(CreateReviewDTO createReviewDTO);
}
