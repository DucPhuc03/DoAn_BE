package do_an.traodoido.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeNotificationPayload {
    private Long tradeId;
    private String requesterName;
    private Long conversationId;
    private String notifyContent;
}
