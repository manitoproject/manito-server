package manito.server.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserInfoResponseDto {
    private Long id;

    private String email;

    private String nickname;

    private String originName;

    private String provider;

    private LocalDateTime regDate;

    @Builder
    public UserInfoResponseDto(Long id, String email, String nickname, String originName, String provider, LocalDateTime regDate) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.originName = originName;
        this.provider = provider;
        this.regDate = regDate;
    }
}
