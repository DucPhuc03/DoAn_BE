package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateReviewDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "API quản lý đánh giá người dùng")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {
    private final ReviewService reviewService;
    
    @Operation(summary = "Tạo đánh giá mới", description = "Tạo một đánh giá cho người dùng sau khi hoàn thành giao dịch. Yêu cầu xác thực.")
    @PostMapping
    public ResponseEntity<RestResponse<String>> createReview(@RequestBody CreateReviewDTO createReviewDTO) {

        return ResponseEntity.ok(reviewService.createReview(createReviewDTO));
    }

    @Operation(summary = "Lấy đánh giá của người dùng", description = "Lấy tất cả đánh giá của một người dùng cụ thể. Yêu cầu xác thực.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<RestResponse<?>> getReviewUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewUser(userId));
    }
}
