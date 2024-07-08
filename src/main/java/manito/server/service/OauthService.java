package manito.server.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import manito.server.dto.AccessTokenRequestDto;
import manito.server.dto.AccessTokenResponseDto;
import manito.server.dto.RefreshTokenResponseDto;
import manito.server.dto.ResponseDto;
import manito.server.entity.User;
import manito.server.exception.CustomException;
import manito.server.exception.ErrorCode;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OauthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;

    //카카오 로그인
    public ResponseDto<AccessTokenResponseDto> loginWithKakao(HttpServletResponse response, AccessTokenRequestDto requestBody) {
        System.out.println("<<< OauthService>> code = " + requestBody.getCode());

        String code = requestBody.getCode();

        System.out.println("<<< OauthService >>> accessToken = " + code);
        User user = kakaoOauthService.saveUser(code);

        String token = getTokens(user.getId(), response);

        AccessTokenResponseDto accessToken = AccessTokenResponseDto.builder()
                .accessToken(token)
                .build();

        return new ResponseDto<>("Success", null, accessToken);
    }

    //액세스토큰, 리프레시토큰 생성
    public String getTokens(Long id, HttpServletResponse response) {
        final String accessToken = jwtTokenService.createAccessToken(id.toString());
        final String refreshToken = jwtTokenService.createRefreshToken();

        User user = userService.getUser(id);
        user.setRefreshToken(refreshToken);
        userService.updateRefreshToken(user);

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        return accessToken;
    }

    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public ResponseDto<AccessTokenResponseDto> refreshAccessToken(HttpServletRequest request, String refreshToken) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();
        if (list == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(
                Collectors.toList()).get(0);

        if (refreshTokenCookie == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        User user = userService.getUser(refreshToken);
        if(user == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(!jwtTokenService.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String token = jwtTokenService.createAccessToken(user.getId().toString());

        AccessTokenResponseDto accessToken = AccessTokenResponseDto.builder()
                .accessToken(token)
                .build();

        return new ResponseDto<>("Success", null, accessToken);
    }
}