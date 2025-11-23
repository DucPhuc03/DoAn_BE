package do_an.traodoido.controller;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/conversation")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping
    public ResponseEntity<RestResponse<List<ResConversationDTO>>> getConversationIds() {
        return ResponseEntity.ok(conversationService.getConversationIds());
    }
}
