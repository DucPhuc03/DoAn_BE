package do_an.traodoido.service.impl;

import do_an.traodoido.dto.response.ResFollowDTO;
import do_an.traodoido.dto.response.RestResponse;
import do_an.traodoido.entity.Follow;
import do_an.traodoido.entity.User;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.exception.UnauthorizedAccessException;
import do_an.traodoido.repository.FollowRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.FollowService;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public RestResponse<String> followUser(Long userIdToFollow) {
        User currentUser = resolveCurrentUser();
        User userToFollow = userRepository.findById(userIdToFollow).orElseThrow(() -> new InvalidException("User not found"));
        Follow alreadyFollowing = followRepository.findByFollowerIdAndFollowingId(currentUser.getId(), userToFollow.getId());
        if (alreadyFollowing!=null) {
            followRepository.delete(alreadyFollowing);
        }

        else {

            Follow follow = new Follow();
            follow.setFollower(currentUser);
            follow.setFollowing(userToFollow);
            followRepository.save(follow);
        }

        return RestResponse.<String>builder()
                .code(200)
                .message("Followed user successfully")
                .data("Followed user successfully")
                .build();

    }



    @Override
    public RestResponse<String> unfollowUser(Long userIdToUnfollow) {
        return null;
    }

    @Override
    public List<ResFollowDTO> getFollowers(Long userId) {
        List<Follow> follow = followRepository.findByFollowingId(userId);
        List<ResFollowDTO> resFollowDTOS = follow.stream().map(f -> {
            User following = f.getFollower();
            return ResFollowDTO.builder()
                    .userId(following.getId())
                    .fullName(following.getFullName())
                    .avatarUrl(following.getAvatarUrl())
                    .build();
        }).toList();
        return resFollowDTOS;
    }


    @Override
    public List<ResFollowDTO> getFollowing(Long userId) {
        List<Follow> follow = followRepository.findByFollowerId(userId);
        List<ResFollowDTO> resFollowDTOS = follow.stream().map(f -> {
            User following = f.getFollowing();
            return ResFollowDTO.builder()
                    .userId(following.getId())
                    .fullName(following.getFullName())
                    .avatarUrl(following.getAvatarUrl())
                    .build();
        }).toList();
        return resFollowDTOS;
    }


    private User resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidException("User", "username", username);
        }
        return user;
    }
}
