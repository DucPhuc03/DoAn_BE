package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResConversationDTO {
    private Long conversationId;
    private Long tradeId;
    private String itemTitle;
    private String itemImage;
    private String username;
    private String userAvatar;
    List<ResMessageDTO> messages;
    TradeMeetingDTO meeting;
}
