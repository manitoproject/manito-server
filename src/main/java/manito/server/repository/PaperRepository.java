package manito.server.repository;

import java.util.List;
import manito.server.entity.Paper;
import manito.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findByUser(User user);

    Paper findTopByUserOrderByIdDesc(User user);

    List<Paper> findByCategory(String category);
}