package manito.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String originName;

    private String profileImage;

    private String provider;

    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime regDate;

    private String isOriginProfile;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateIsOriginProfile(String isOriginProfile) {
        this.isOriginProfile = isOriginProfile;
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public void updateOldUserInfo(String email, String originName, String profileImage) {
        this.email = email;
        this.originName = originName;
        this.profileImage = profileImage;
    }
}
