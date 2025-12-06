package do_an.traodoido.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Description;

@Entity
@Table(name = "follows")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Description("Mối quan hệ theo dõi giữa người dùng")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
}
