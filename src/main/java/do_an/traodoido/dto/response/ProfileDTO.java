package do_an.traodoido.dto.response;

import do_an.traodoido.enums.FollowStatus;
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
    private int level;
    private List<ResPostDTO> posts;
    private List<ResPostDTO> likedPosts;
    List<ResFollowDTO> followers;
    List<ResFollowDTO> following;
    private boolean canSetting;
    private boolean displayHistory;
    private FollowStatus followStatus;
    private boolean canReport;
    private boolean canEditAddress;
    private boolean canEditBio;
    private int trades;

}
