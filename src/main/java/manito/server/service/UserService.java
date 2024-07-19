package manito.server.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.auth.SecurityUtil;
import manito.server.dto.KakaoUserInfoResponseDto;
import manito.server.dto.NicknameRequestDto;
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
                    .provider(user.getProvider())
                    .regDate(user.getRegDate())
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

    public ResponseDto changeNickname(RequestHeaderDto requestHeader, NicknameRequestDto requestBody) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = getUser(userId);

        user.updateNickname(requestBody.getNickname());

        userRepository.saveAndFlush(user);

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }

    /**
     * 카카오 API에서 가져온 유저정보를 DB에 저장
     */
    public void saveUser(KakaoUserInfoResponseDto kakaoUser){

//        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);

        User user = User.builder()
                .id(kakaoUser.getId())
                .email(kakaoUser.getKakaoAccount().getEmail())
                .nickname(kakaoUser.getKakaoAccount().getProfile().getNickName())
                .originName(kakaoUser.getKakaoAccount().getProfile().getNickName())
                .provider("KAKAO")
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
