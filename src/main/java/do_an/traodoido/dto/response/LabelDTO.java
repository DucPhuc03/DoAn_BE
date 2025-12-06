package do_an.traodoido.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LabelDTO {
    private String name;
    private float confidence;
}
