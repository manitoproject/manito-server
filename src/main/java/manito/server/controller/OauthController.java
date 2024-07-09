package manito.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import manito.server.dto.AccessTokenRequestDto;
import manito.server.service.OauthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth")
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @PostMapping("/kakao")
    public ResponseEntity<?> kakao(HttpServletResponse response, @RequestBody AccessTokenRequestDto requestBody) {
        return new ResponseEntity<>(oauthService.loginWithKakao(response, requestBody), HttpStatus.OK);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> tokenRefresh(HttpServletRequest request) {
        return new ResponseEntity<>(oauthService.refreshAccessToken(request), HttpStatus.OK);
    }
}
