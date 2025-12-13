package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendedItem {
    private Long id;
    double similarity;
}
