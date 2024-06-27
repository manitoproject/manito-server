package manito.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import manito.server.auth.OAuthProvider;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private OAuthProvider oAuthProvider;


}
