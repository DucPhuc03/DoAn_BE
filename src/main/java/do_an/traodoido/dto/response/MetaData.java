package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaData {
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
}
