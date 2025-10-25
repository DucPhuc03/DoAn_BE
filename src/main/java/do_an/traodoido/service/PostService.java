package do_an.traodoido.service;

import do_an.traodoido.dto.response.RestResponse;

public interface PostService {
    RestResponse<String> createPost();
}
