package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResConversationDTO {
    private Long conversationId;

    private String itemTitle;
    List<ResMessageDTO> messages;
}
