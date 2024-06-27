package manito.server.dto;

import manito.server.auth.OAuthProvider;

public interface OAuthUserInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
