package do_an.traodoido.entity;

import do_an.traodoido.enums.TradeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trades")
@Schema(description = "Giao dịch trao đổi đồ")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateCompleted;
    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus;

    private Long userStart;
    private Long userEnd;
    @ManyToOne
    @JoinColumn(name = "user_requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "user_owner_id", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "requester_post_id", nullable = true)
    private Post requesterPost;

    @ManyToOne
    @JoinColumn(name = "owner_post_id", nullable = false)
    private Post ownerPost;

    @OneToOne(mappedBy = "trade", cascade = CascadeType.ALL, orphanRemoval = true)
    private Conversation conversation;
    private LocalDateTime createdAt;

}
