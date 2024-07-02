package manito.server.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import manito.server.service.OauthService;
import manito.server.dto.AccessTokenRequestDto;
import manito.server.dto.OAuthResponseDto;
import manito.server.dto.RefreshTokenResponseDto;
import manito.server.exception.CustomException;
import manito.server.exception.ErrorCode;
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
    public OAuthResponseDto kakao(HttpServletResponse response,@RequestBody AccessTokenRequestDto requestBody) {
        System.out.println("<<< OauthController>> code = " + requestBody.getCode());

        String accessToken = oauthService.loginWithKakao(requestBody.getCode(), response);

        OAuthResponseDto oAuthResponseDto = new OAuthResponseDto(accessToken);

        return oAuthResponseDto;
    }

    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto tokenRefresh(HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();
        if (list == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(
                Collectors.toList()).get(0);

        if (refreshTokenCookie == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return refreshTokenResponseDto;
    }
}
