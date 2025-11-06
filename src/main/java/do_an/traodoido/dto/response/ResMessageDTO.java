package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResMessageDTO {
    private Long id;
    private String senderName;
    private LocalDateTime timestamp;
    private String avatarUrl;
    private boolean isRead;
    private String content;
}
