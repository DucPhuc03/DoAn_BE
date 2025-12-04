package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.repository.FollowRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.FollowService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public RestResponse<String> followUser(Long userIdToFollow) {
        User currentUser = userService.getCurrentUser();
        User userToFollow = userRepository.findById(userIdToFollow).orElseThrow(() -> new InvalidException("User not found"));



        return null;
    }

    @Override
    public RestResponse<String> unfollowUser(Long userIdToUnfollow) {
        return null;
    }
}
