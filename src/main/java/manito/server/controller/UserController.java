package manito.server.controller;

import lombok.RequiredArgsConstructor;
import manito.server.auth.SecurityUtil;
import manito.server.entity.User;
import manito.server.exception.CustomException;
import manito.server.exception.ErrorCode;
import manito.server.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    //todo: 왜 403 forbidden 에러 뜨는지?
    /**
     * 유저정보 조회 API
     * @return
     */
    @GetMapping("/info")
    public User info() {
        final long userId = SecurityUtil.getCurrentUserId();

        User user = userService.getUser(userId);

        System.out.println("user = " + user);

        if(user == null)
            throw new CustomException(ErrorCode.NOT_EXIST_USER);

        return user;
    }
}
