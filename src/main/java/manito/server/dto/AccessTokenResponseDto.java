package manito.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccessTokenResponseDto {
    private String accessToken;

    @JsonInclude(Include.NON_NULL)
    private String isNewUser;

    @JsonInclude(Include.NON_NULL)
    private UserDto userInfo;

    @Builder
    public AccessTokenResponseDto(String accessToken, String isNewUser, UserDto userInfo) {
        this.accessToken = accessToken;
        this.isNewUser = isNewUser;
        this.userInfo = userInfo;
    }
}
