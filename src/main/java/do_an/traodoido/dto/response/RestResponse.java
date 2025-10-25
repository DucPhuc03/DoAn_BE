package do_an.traodoido.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RestResponse<T> {
    private int code;
    private String message;
    private T data;

}
