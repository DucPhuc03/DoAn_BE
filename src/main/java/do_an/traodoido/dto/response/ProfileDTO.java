package do_an.traodoido.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String avatarUrl;
    private String bio;
    private String address;
    private List<ResPostDTO> posts;
    private List<ResPostDTO> likedPosts;

    private boolean canSetting;
    private boolean displayHistory;
    private boolean isFollowing;
    private boolean canFollow;
    private boolean canReport;
    private boolean canEditAddress;
    private boolean canEditBio;
    private int trades;

}
