package do_an.traodoido.repository;

import do_an.traodoido.entity.Post;
import do_an.traodoido.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdAndPostStatusNotIn(Long user_id, List<PostStatus> postStatus);

    @Query("""
        SELECT p FROM Post p 
        LEFT JOIN p.category c
        WHERE (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))
          AND (:categoryName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%')))
          AND p.postStatus NOT IN (:excludedStatus)
        """)
    Page<Post> searchPostsByTitleAndCategory(
            @Param("title") String title,
            @Param("categoryName") String categoryName,
            @Param("excludedStatus") List<PostStatus> excludedStatus,
            Pageable pageable
    );

    Page<Post> findByPostStatusNotIn(List<PostStatus> excludedStatus, Pageable pageable);


    List<Post> findAllByPostStatus(PostStatus postStatus);
    @Query("""
    SELECT p.id
    FROM Post p
    WHERE p.user.id <> :userId
      AND p.postStatus <> do_an.traodoido.enums.PostStatus.COMPLETED
      AND p.postStatus <> do_an.traodoido.enums.PostStatus.DELETED
""")
    List<Long> findAllPostId(@Param("userId") Long userId);

    @Query("""
    SELECT p.id
    FROM Post p
    WHERE p.postStatus = do_an.traodoido.enums.PostStatus.AVAILABLE
    ORDER BY p.likeCount DESC
""")
    List<Long> findTop10PopularPosts(Pageable pageable);
}
