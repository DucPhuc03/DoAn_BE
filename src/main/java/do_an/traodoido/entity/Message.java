package do_an.traodoido.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "messages")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Tin nhắn trong cuộc trò chuyện")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    private String content;
    private LocalDateTime timestamp;

    private boolean isRead;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;
}
