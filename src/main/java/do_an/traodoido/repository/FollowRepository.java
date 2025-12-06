package do_an.traodoido.repository;

import do_an.traodoido.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.Flow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {


    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
    int countFollowers(Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    int countFollowing(Long userId);
    List<Follow> findByFollowerId(Long userId);
    List<Follow> findByFollowingId(Long userId);

    Follow findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);


}
