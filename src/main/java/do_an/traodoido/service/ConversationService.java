package do_an.traodoido.service;

import do_an.traodoido.dto.response.ResConversationDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface ConversationService {
    RestResponse<List<ResConversationDTO>> getConversationIds();
}
