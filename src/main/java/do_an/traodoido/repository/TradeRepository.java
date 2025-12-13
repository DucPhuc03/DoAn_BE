package do_an.traodoido.repository;

import do_an.traodoido.entity.Post;
import do_an.traodoido.entity.Trade;
import do_an.traodoido.enums.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository  extends JpaRepository<Trade, Long> {
    List<Trade> findByRequesterIdOrOwnerId(Long requesterId, Long ownerId);

    boolean existsByUserEndOrUserStartAndId(Long userEnd, Long userStart, Long id);
    @Query("""
    SELECT COUNT(t)
    FROM Trade t
    WHERE (t.owner.id = :userId OR t.requester.id = :userId)
      AND t.tradeStatus = :status
""")
    int countTradesOfUser(
            @Param("userId") Long userId,
            @Param("status") TradeStatus status
    );

    @Query("""
    SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
    FROM Trade t
    WHERE (t.userEnd = :userId OR t.userStart = :userId)
      AND t.id = :tradeId
""")
    boolean isParticipant(@Param("userId") Long userId, @Param("tradeId") Long tradeId);

    /**
     * Lấy danh sách bài Post của người đối tác trong giao dịch.
     * Nếu user là requester thì trả về ownerPost, ngược lại trả về requesterPost.
     */
    @Query("""
    SELECT CASE
                          WHEN t.requester.id = :userId THEN t.ownerPost
                          ELSE t.requesterPost
                      END
    FROM Trade t
    WHERE (t.requester.id = :userId OR t.owner.id = :userId)
      AND t.tradeStatus = "COMPLETED"
""")
    List<Post> findCounterpartyPosts(@Param("userId") Long userId);




}
