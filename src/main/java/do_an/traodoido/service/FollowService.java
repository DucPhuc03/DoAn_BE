package do_an.traodoido.service;

import do_an.traodoido.dto.response.RestResponse;

public interface FollowService {
    RestResponse<String> followUser(Long userIdToFollow);
    RestResponse<String> unfollowUser(Long userIdToUnfollow);

}
