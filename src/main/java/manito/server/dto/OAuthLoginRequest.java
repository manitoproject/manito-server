package manito.server.dto;

import manito.server.auth.OAuthProvider;
import org.springframework.util.MultiValueMap;

public interface OAuthLoginRequest {
    OAuthProvider oAuthProvider();

    MultiValueMap<String, String> makeBody();
}
