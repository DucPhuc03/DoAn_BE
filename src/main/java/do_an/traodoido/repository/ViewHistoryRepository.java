package do_an.traodoido.repository;

import do_an.traodoido.entity.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory,Long> {
    Optional<ViewHistory> findByPostIdAndUserId(Long postId,Long userId);

    List<ViewHistory> findAllByUserId(Long userId);

    @Query("""
    SELECT v.post.id
    FROM ViewHistory v
    WHERE v.user.id = :userId
""")
    List<Long> findViewedPostIds(@Param("userId") Long userId);

}
