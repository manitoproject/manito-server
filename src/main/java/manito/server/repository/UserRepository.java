package manito.server.repository;

import java.util.Optional;
import manito.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRefreshToken(String refreshToken);
}
