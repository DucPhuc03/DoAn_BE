package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateReviewDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/view")
@RequiredArgsConstructor
public class ViewHistoryController {
    private final ViewHistoryService viewHistoryService;

    @PostMapping("/{postId}")
    public ResponseEntity<RestResponse<String>> createReview(@PathVariable("postId") Long id) {

        return ResponseEntity.ok(viewHistoryService.createViewHistory(id));
    }


}
