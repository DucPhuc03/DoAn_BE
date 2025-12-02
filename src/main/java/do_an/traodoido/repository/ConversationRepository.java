package do_an.traodoido.repository;

import do_an.traodoido.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByParticipant1_IdOrParticipant2_IdOrderByIdDesc(Long participant1Id, Long participant2Id);
}
