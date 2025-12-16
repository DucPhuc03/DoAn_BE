package do_an.traodoido.repository;

import do_an.traodoido.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findAllByUserIdOrderByTimeDesc(Long id);
}
