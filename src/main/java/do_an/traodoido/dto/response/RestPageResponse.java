package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestPageResponse<T> {
    private int code;
    private String message;
    private T data;
    private MetaData metaData;
}
