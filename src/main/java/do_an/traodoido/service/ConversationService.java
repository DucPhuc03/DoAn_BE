package do_an.traodoido.service;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.dto.response.UserChat;

import java.util.List;
import java.util.Map;

public interface ConversationService {
    RestResponse<Map<UserChat,List<ResConversationDTO>>> getConversationIds();
    void deleteConversation(Long id);
}
