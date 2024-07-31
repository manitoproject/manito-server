package manito.server.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.auth.SecurityUtil;
import manito.server.dto.KakaoUserInfoResponseDto;
import manito.server.dto.RequestHeaderDto;
import manito.server.dto.ResponseDto;
import manito.server.dto.UserDto;
import manito.server.entity.User;
import manito.server.repository.UserRepository;
import manito.server.util.AppUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            return null;

        return user.get();
    }

    public User getUser(String refreshToken) {
        Optional<User> user = userRepository.findByRefreshToken(refreshToken);
        if (user.isEmpty())
            return null;

        return user.get();
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void updateRefreshToken(User user) {
        userRepository.save(user);
    }

    public ResponseDto getCurrentUserInfo(RequestHeaderDto requestHeader) {
        UserDto userDto = new UserDto();

        try {
            Long userId = SecurityUtil.getCurrentUserId();

            User user = getUser(userId);

            userDto = UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .originName(user.getOriginName())
                    .profileImage(user.getProfileImage())
                    .provider(user.getProvider())
                    .regDate(user.getRegDate())
                    .isOriginProfile(user.getIsOriginProfile())
                    .build();
        } catch (Exception e) {
            log.error("{}|UserService.getCurrentUserInfo|error = {}", requestHeader.getAuthorization(), e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return new ResponseDto<>("Success", null, userDto);
    }

    public ResponseDto changeNickname(RequestHeaderDto requestHeader, UserDto requestBody) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = getUser(userId);

        user.updateNickname(requestBody.getNickname());

        userRepository.saveAndFlush(user);

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }

    public ResponseDto changeProfile(RequestHeaderDto requestHeader, UserDto requestBody) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = getUser(userId);

        user.updateIsOriginProfile(requestBody.getIsOriginProfile());

        userRepository.saveAndFlush(user);

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }

    /**
     * 카카오 API에서 가져온 유저정보를 DB에 저장
     */
    public void saveNewUser(KakaoUserInfoResponseDto kakaoUser){
        User user = User.builder()
                .id(kakaoUser.getId())
                .email(kakaoUser.getKakaoAccount().getEmail())
                .nickname(kakaoUser.getKakaoAccount().getProfile().getNickName())
                .originName(kakaoUser.getKakaoAccount().getProfile().getNickName())
                .profileImage(kakaoUser.getKakaoAccount().getProfile().getProfileImageUrl())
                .provider("KAKAO")
                .regDate(LocalDateTime.now())
                .build();

        userRepository.saveAndFlush(user);
    }

    public void saveOldUser(KakaoUserInfoResponseDto kakaoUser){
        User user = userRepository.findById(kakaoUser.getId()).get();
        user.updateOldUserInfo(kakaoUser.getKakaoAccount().getEmail(),
                kakaoUser.getKakaoAccount().getProfile().getNickName(),
                kakaoUser.getKakaoAccount().getProfile().getProfileImageUrl());

        userRepository.saveAndFlush(user);
    }

    public ResponseDto<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("refreshToken")).findFirst();

            if (refreshTokenCookie.isPresent()) {
                refreshTokenCookie.get().setMaxAge(0);
                response.addCookie(refreshTokenCookie.get());
            } // refreshTokenCookie 삭제. HttpOnly여서 서버에서 삭제

            Long userId = SecurityUtil.getCurrentUserId();
            User user = getUser(userId);
            user.deleteRefreshToken();
            userRepository.saveAndFlush(user);

        } catch (Exception e) {
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(AppUtil.LOGOUT_FAILED)
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }
}
