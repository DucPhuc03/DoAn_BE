package do_an.traodoido.entity;

import do_an.traodoido.enums.CategoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
@Schema(description = "Danh mục sản phẩm")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String image;
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;
}
