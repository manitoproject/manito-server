package manito.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserDto {
    @JsonInclude(Include.NON_NULL)
    private Long id;

    @JsonInclude(Include.NON_NULL)
    private String email;

    @JsonInclude(Include.NON_NULL)
    private String nickname;

    @JsonInclude(Include.NON_NULL)
    private String originName;

    private String profileImage;

    @JsonInclude(Include.NON_NULL)
    private String provider;

    @JsonInclude(Include.NON_NULL)
    private LocalDateTime regDate;

    private String isOriginProfile;

    @Builder
    public UserDto(Long id, String email, String nickname, String originName, String profileImage, String provider, LocalDateTime regDate, String isOriginProfile) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.originName = originName;
        this.profileImage = profileImage;
        this.provider = provider;
        this.regDate = regDate;
        this.isOriginProfile = isOriginProfile;
    }
}
