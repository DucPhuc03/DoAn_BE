package do_an.traodoido.repository;

import do_an.traodoido.entity.Trade;
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
    SELECT CASE WHEN COUNT(t) > 0 THEN TRUE ELSE FALSE END
    FROM Trade t
    WHERE (t.userEnd = :userId OR t.userStart = :userId)
      AND t.id = :tradeId
""")
    boolean isParticipant(@Param("userId") Long userId, @Param("tradeId") Long tradeId);
}
