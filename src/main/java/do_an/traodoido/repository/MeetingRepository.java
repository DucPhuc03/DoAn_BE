package do_an.traodoido.repository;

import do_an.traodoido.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m " +
            "JOIN m.trade t " +
            "WHERE t.requester.id = :userId OR t.owner.id = :userId"
            + " ORDER BY m.meetingDate DESC"
    )
    List<Meeting> findByUserId(@Param("userId") Long userId);

    Meeting findByTradeId(Long tradeId);
}
