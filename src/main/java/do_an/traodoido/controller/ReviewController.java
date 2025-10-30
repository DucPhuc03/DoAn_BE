package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateReviewDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity<RestResponse<String>> createReview(@RequestBody CreateReviewDTO createReviewDTO) {

        return ResponseEntity.ok(reviewService.createReview(createReviewDTO));
    }
}
