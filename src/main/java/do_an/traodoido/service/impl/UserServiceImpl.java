package do_an.traodoido.service.impl;

import do_an.traodoido.dto.request.UpdateProfileDTO;
import do_an.traodoido.dto.response.*;
import do_an.traodoido.entity.User;
import do_an.traodoido.enums.TradeStatus;
import do_an.traodoido.enums.UserStatus;
import do_an.traodoido.exception.InvalidException;
import do_an.traodoido.exception.UnauthorizedAccessException;
import do_an.traodoido.repository.TradeRepository;
import do_an.traodoido.repository.UserRepository;
import do_an.traodoido.service.LikeService;
import do_an.traodoido.service.PostService;
import do_an.traodoido.service.S3Service;
import do_an.traodoido.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final S3Service s3Service;
    private final TradeRepository tradeRepository;
    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        return authentication.getName();
    }

    @Override
    public User getCurrentUser() {
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidException("User", "username", username);
        }
        return user;
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            Object userClaim = jwt.getClaim("user");
            if (userClaim instanceof java.util.Map<?, ?> map && map.get("id") != null) {
                return Long.valueOf(map.get("id").toString());
            }
        }
        // Fallback: resolve by username from authentication name
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new InvalidException("User", "username", username);
        }
        return user.getId();
    }

    @Override
    public RestResponse<ProfileDTO> getProfile(Long userId) {
        User currentUser = getCurrentUser();
        boolean isOwnProfile = currentUser.getId().equals(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidException("User", userId));
        List<ResPostDTO> userPosts = postService.getPostByUserId(userId).getData();
        ProfileDTO profileDTO = ProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .bio(user.getBio())
                .address(user.getAddress())
                .avatarUrl(user.getAvatarUrl())
                .posts(userPosts)
                .likedPosts(postService.getLikedPostsByUser(userId))
                .canSetting(isOwnProfile)
                .displayHistory(isOwnProfile)
                .canEditAddress(true)
                .canEditBio(true)
                .canFollow(true)
                .trades(tradeRepository.countTradesOfUser(userId, TradeStatus.COMPLETED))
                .build();

        return RestResponse.<ProfileDTO>builder()
                .code(1000)
                .message("Profile retrieved successfully")
                .data(profileDTO)
                .build();
    }

    @Override
    public RestResponse<String> updateAvatar(MultipartFile avatarFile) throws IOException {
        Long userId= getCurrentUserId();
        String avatarUrl = s3Service.uploadFile(avatarFile, "profile");
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidException("User", userId));
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Avatar updated successfully")
                .data(avatarUrl)
                .build();
    }

    @Override
    public RestResponse<String> updateProfile(UpdateProfileDTO updateProfileDTO) {
        User user = getCurrentUser();
        user.setFullName(updateProfileDTO.getFullName());
        user.setPhoneNumber(updateProfileDTO.getPhoneNumber());
        user.setAddress(updateProfileDTO.getAddress());
        user.setBio(updateProfileDTO.getBio());
        userRepository.save(user);
        return RestResponse.<String>builder()
                .code(1000)
                .message("Profile updated successfully")
                .data("Profile updated")
                .build();
    }

    @Override
    public RestResponse<String> updateStatus(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidException("User", userId));
        user.setStatus(user.getStatus() == UserStatus.ACTIVE ?
                UserStatus.BANNED :UserStatus.ACTIVE);
        userRepository.save(user);
        return RestResponse.<String>builder()
                .code(1000)
                .message("User status updated successfully")
                .data("User status updated")
                .build();
    }

    @Override
    public RestResponse<List<ResUserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<ResUserDTO> resUserDTOs = users.stream().map(user -> ResUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .build()).toList();
        return RestResponse.<List<ResUserDTO>>builder()
                .code(1000)
                .message("Users retrieved successfully")
                .data(resUserDTOs)
                .build();
    }
}


