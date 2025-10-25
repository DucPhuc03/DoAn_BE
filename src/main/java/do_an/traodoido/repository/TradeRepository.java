package do_an.traodoido.repository;

import do_an.traodoido.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository  extends JpaRepository<Trade, Long> {
}
