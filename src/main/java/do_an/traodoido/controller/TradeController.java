package do_an.traodoido.controller;

import do_an.traodoido.dto.request.CreateTradeDTO;
import do_an.traodoido.dto.request.UpdateTradePostDTO;
import do_an.traodoido.dto.response.ResTradeDTO;
import do_an.traodoido.dto.response.ResTradeDetailDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
@Tag(name = "Trade", description = "API quản lý giao dịch trao đổi đồ")
@SecurityRequirement(name = "Bearer Authentication")
public class TradeController {

    private final TradeService tradeService;
    
    @Operation(summary = "Tạo yêu cầu trao đổi", description = "Tạo một yêu cầu trao đổi đồ giữa hai bài đăng. Yêu cầu xác thực.")
    @PostMapping
    public ResponseEntity<RestResponse<String>> createTrade(@RequestBody CreateTradeDTO createTradeDTO) {

        return ResponseEntity.ok(tradeService.createTrade(createTradeDTO));
    }

    @Operation(summary = "Lấy chi tiết giao dịch", description = "Lấy thông tin chi tiết của một giao dịch trao đổi theo ID. Yêu cầu xác thực.")
    @GetMapping("/{tradeId}")
    public ResponseEntity<RestResponse<ResTradeDetailDTO>> getTradeDetails(@PathVariable Long tradeId) {
        return ResponseEntity.ok(tradeService.getTradeDetails(tradeId));
    }

    @Operation(summary = "Cập nhật bài đăng trong giao dịch", description = "Cập nhật bài đăng của người yêu cầu trong một giao dịch trao đổi. Yêu cầu xác thực.")
    @PostMapping("/update-post")
    public ResponseEntity<RestResponse<String>> updateTradePost(@RequestBody UpdateTradePostDTO updateTradePostDTO) {
        return ResponseEntity.ok(tradeService.updateRequesterPost(updateTradePostDTO));
    }

    @Operation(summary = "Cập nhật trạng thái giao dịch", description = "Cập nhật trạng thái của giao dịch trao đổi (ví dụ: ACCEPTED, REJECTED, COMPLETED). Yêu cầu xác thực.")
    @PatchMapping("/{tradeId}")
    public ResponseEntity<RestResponse<String>> updateTradeStatus(@PathVariable Long tradeId) {
        return ResponseEntity.ok(tradeService.updateTradeStatus(tradeId));
    }

    @Operation(summary = "Lấy danh sách giao dịch của người dùng", description = "Lấy tất cả giao dịch trao đổi của người dùng hiện tại. Yêu cầu xác thực.")
    @GetMapping("/user")
    public ResponseEntity<RestResponse<List<ResTradeDTO>>> getTradesByUser() {
        return ResponseEntity.ok(tradeService.getTradesByUser());
    }
}
