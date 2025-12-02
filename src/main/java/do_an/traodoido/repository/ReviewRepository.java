package do_an.traodoido.repository;

import do_an.traodoido.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<Review, Long> {
    List<Review> findByReviewedId(Long reviewedId);

    boolean existsByReviewerIdAndTradeId( Long reviewedId, Long tradeId);
}
