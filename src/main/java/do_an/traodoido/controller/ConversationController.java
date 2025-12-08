package do_an.traodoido.controller;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.UserChat;
import do_an.traodoido.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/conversation")
@RequiredArgsConstructor
@Tag(name = "Conversation", description = "API quản lý cuộc trò chuyện")
@SecurityRequirement(name = "Bearer Authentication")
public class ConversationController {
    private final ConversationService conversationService;

    @Operation(summary = "Lấy danh sách cuộc trò chuyện", description = "Lấy danh sách tất cả cuộc trò chuyện của người dùng hiện tại. Yêu cầu xác thực.")
    @GetMapping
    public ResponseEntity<RestResponse<Map<UserChat,List<ResConversationDTO>>>> getConversationIds() {
        return ResponseEntity.ok(conversationService.getConversationIds());
    }
}
