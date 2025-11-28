package do_an.traodoido.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
