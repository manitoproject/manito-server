package manito.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    private Long id;

    private String email;

    private String platform;

    private String refreshToken;
}
