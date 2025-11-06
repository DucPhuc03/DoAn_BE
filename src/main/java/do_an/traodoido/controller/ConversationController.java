package do_an.traodoido.controller;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/conversation")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping
    public RestResponse<java.util.List<ResConversationDTO>> getConversationIds() {
        return conversationService.getConversationIds();
    }
}
