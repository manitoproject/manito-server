package manito.server.repository;

import java.util.List;
import java.util.Optional;
import manito.server.entity.Message;
import manito.server.entity.Paper;
import manito.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUser(User user);

    List<Message> findByPaper(Paper paper);

    Optional<Message> findByPaperAndPosition(Paper paper, Integer position);
}
