package do_an.traodoido.repository;

import do_an.traodoido.entity.Like;
import do_an.traodoido.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long>{
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    List<Like> findByUserId(Long userId);
    @Query("""
    SELECT l.post.id
    FROM Like l
    WHERE l.user.id = :userId
""")
    List<Long> findLikedPostIds(@Param("userId") Long userId);


    boolean existsByPostIdAndUserId(Long postId, Long userId);
}
