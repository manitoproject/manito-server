package manito.server.auth;

import manito.server.dto.OAuthLoginRequest;
import manito.server.dto.OAuthUserInfoResponse;

public interface OAuthClient {
    OAuthProvider oAuthProvider();

    String requestAccessToken(OAuthLoginRequest request);

    OAuthUserInfoResponse requestOAuthUserInfo(String accessToken);
}
