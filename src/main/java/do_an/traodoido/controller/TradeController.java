package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.request.UpdateTradePostDTO;
import do_an.traodoido.dto.response.ResTradeDTO;
import do_an.traodoido.dto.response.ResTradeDetailDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;
    @PostMapping
    public ResponseEntity<RestResponse<String>> createTrade(@RequestBody CreateTradeDTO createTradeDTO) {

        return ResponseEntity.ok(tradeService.createTrade(createTradeDTO));
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<RestResponse<ResTradeDetailDTO>> getTradeDetails(@PathVariable Long tradeId) {
        return ResponseEntity.ok(tradeService.getTradeDetails(tradeId));
    }

    @PostMapping("/update-post")
    public ResponseEntity<RestResponse<String>> updateTradePost(@RequestBody UpdateTradePostDTO updateTradePostDTO) {
        return ResponseEntity.ok(tradeService.updateRequesterPost(updateTradePostDTO));
    }

    @PatchMapping("/{tradeId}")
    public ResponseEntity<RestResponse<String>> updateTradeStatus(@PathVariable Long tradeId) {
        return ResponseEntity.ok(tradeService.updateTradeStatus(tradeId));
    }

    @GetMapping("/user")
    public ResponseEntity<RestResponse<List<ResTradeDTO>>> getTradesByUser() {
        return ResponseEntity.ok(tradeService.getTradesByUser());
    }
}
