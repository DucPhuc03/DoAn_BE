package do_an.traodoido.service;

import do_an.traodoido.dto.response.ResFollowDTO;
import do_an.traodoido.dto.response.RestResponse;

import java.util.List;

public interface FollowService {
    RestResponse<String> followUser(Long userIdToFollow);
    RestResponse<String> unfollowUser(Long userIdToUnfollow);
    List<ResFollowDTO> getFollowers(Long userId);
    List<ResFollowDTO> getFollowing(Long userId);

}
