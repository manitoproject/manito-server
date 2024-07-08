package manito.server.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import manito.server.auth.SecurityUtil;
import manito.server.dto.ResponseDto;
import manito.server.dto.UserInfoResponseDto;
import manito.server.entity.User;
import manito.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty())
            return null;

        return user.get();
    }

    public User getUser(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken);

        return user;
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public void updateRefreshToken(User user) {
        userRepository.save(user);
    }

    public ResponseDto<UserInfoResponseDto> getCurrentUserInfo() {
        Long userId = SecurityUtil.getCurrentUserId();

        User user = getUser(userId);

        UserInfoResponseDto userInfo = UserInfoResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .originName(user.getOriginName())
                .provider(user.getProvider())
                .regDate(user.getRegDate())
                .build();

        return new ResponseDto<>("Success", null, userInfo);
    }
}
