package do_an.traodoido.service;

import do_an.traodoido.dto.response.RestResponse;
import software.amazon.awssdk.services.bedrockruntime.endpoints.internal.Value;

public interface ViewHistoryService {
    RestResponse<String> createViewHistory(Long postId);
}
