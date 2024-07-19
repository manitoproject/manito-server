package manito.server.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.dto.AccessTokenRequestDto;
import manito.server.dto.AccessTokenResponseDto;
import manito.server.dto.KakaoUserInfoResponseDto;
import manito.server.dto.ResponseDto;
import manito.server.dto.UserDto;
import manito.server.entity.User;
import manito.server.exception.CustomException;
import manito.server.exception.ErrorCode;
import manito.server.util.HttpServletUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OauthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;

    //카카오 로그인
    public ResponseDto<AccessTokenResponseDto> loginWithKakao(HttpServletResponse response, AccessTokenRequestDto requestBody) {
        log.info("OauthService.loginWithKakao|requestBody = {}", requestBody);

        String code = requestBody.getCode();
        String token = kakaoOauthService.getKakaoToken(code);

        KakaoUserInfoResponseDto kakaoUser = kakaoOauthService.getKakaoUserInfo(token);
        Long kakoUserId = kakaoUser.getId();

        User user = userService.getUser(kakoUserId);

        UserDto userDto = new UserDto();

        String accessToken = getTokens(kakoUserId, response);
        AccessTokenResponseDto accessTokenResponse = new AccessTokenResponseDto();
        if (user == null) {
            userDto = UserDto.builder()
                    .id(kakaoUser.getId())
                    .email(kakaoUser.getKakaoAccount().getEmail())
//                    .nickname(user.getNickname())
                    .originName(kakaoUser.getKakaoAccount().getProfile().getNickName())
                    .provider("KAKAO")
//                    .regDate(user.getRegDate())
                    .build();

            accessTokenResponse = AccessTokenResponseDto.builder()
                    .accessToken(accessToken)
                    .isNewUser("Y")
                    .userInfo(userDto)
                    .build();
            return new ResponseDto<>("Success", null, accessTokenResponse);
        }

        userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .originName(user.getOriginName())
                .provider("KAKAO")
                .regDate(user.getRegDate())
                .build();

        accessTokenResponse = AccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .isNewUser("N")
                .userInfo(userDto)
                .build();

        userService.saveUser(kakaoUser);

        return new ResponseDto<>("Success", null, accessTokenResponse);
    }

    //액세스토큰, 리프레시토큰 생성
    public String getTokens(Long id, HttpServletResponse response) {
        final String accessToken = jwtTokenService.createAccessToken(id.toString());
        final String refreshToken = jwtTokenService.createRefreshToken();

        User user = userService.getUser(id);
        user.updateRefreshToken(refreshToken);
        userService.updateRefreshToken(user);

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        return accessToken;
    }

    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public ResponseDto<AccessTokenResponseDto> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = HttpServletUtil.getRreshToken(request);

        User user = userService.getUser(refreshToken);

        if(user == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        if(!jwtTokenService.validateToken(refreshToken))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        String token = jwtTokenService.createAccessToken(user.getId().toString());

        AccessTokenResponseDto accessToken = AccessTokenResponseDto.builder()
                .accessToken(token)
                .build();

        return new ResponseDto<>("Success", null, accessToken);
    }
}