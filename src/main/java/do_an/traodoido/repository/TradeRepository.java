package do_an.traodoido.repository;

import do_an.traodoido.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository  extends JpaRepository<Trade, Long> {
    List<Trade> findByRequesterIdOrOwnerId(Long requesterId, Long ownerId);
}
