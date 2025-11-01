package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;
    @PostMapping
    public ResponseEntity<RestResponse<String>> createTrade(@RequestBody CreateTradeDTO createTradeDTO) {

        return ResponseEntity.ok(tradeService.createTrade(createTradeDTO));
    }
}
