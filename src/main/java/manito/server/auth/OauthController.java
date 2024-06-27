package manito.server.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OAuthService oauthService;

    @GetMapping(value = "/login/oauth/kakao", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity getKakaoCode(@RequestParam String code) {
        return new ResponseEntity<>(oauthService.getKakaoAccessToken(code), HttpStatus.OK);
    }
}
