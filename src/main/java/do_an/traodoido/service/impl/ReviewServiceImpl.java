package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.CreatePostDTO;
import do_an.traodoido.dto.request.CreateReviewDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Review;
import do_an.traodoido.entity.Trade;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.ReviewRepository;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.ReviewService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    @Override
    public RestResponse<String> createReview(CreateReviewDTO createReviewDTO) {

        User reviewer = userService.getCurrentUser();
        User reviewed = userRepository.findById(createReviewDTO.getReviewedId()).orElseThrow(()->new InvalidException("Reviewed user not found"));
        Trade trade = tradeRepository.findById(createReviewDTO.getTradeId()).orElseThrow(()->new InvalidException("Trade not found"));
        Review review=Review.builder()
                .rating(createReviewDTO.getRating())
                .comment(createReviewDTO.getComment())
                .reviewDate(LocalDate.now())
                .reviewer(reviewer)
                .reviewed(reviewed)
                .trade(trade)
                .build();

        reviewRepository.save(review);

        return RestResponse.<String>builder()
                .code(1000)
                .message("Review created successfully")
                .data("Review ID: " + review.getId())
                .build();
    }

    @Override
    public RestResponse<String> getReviewUser(Long userId) {
        return null;
    }
}
