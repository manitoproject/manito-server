package manito.server.auth;

import lombok.RequiredArgsConstructor;
import manito.server.dto.OAuthLoginRequest;
import manito.server.dto.OAuthUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoClient implements OAuthClient {
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Override
    public OAuthProvider oAuthProvider() {
        return null;
    }

    @Override
    public String requestAccessToken(OAuthLoginRequest request) {
        return null;
    }

    @Override
    public OAuthUserInfoResponse requestOAuthUserInfo(String accessToken) {
        return null;
    }
}
