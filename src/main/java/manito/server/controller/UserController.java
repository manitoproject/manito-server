package manito.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import manito.server.service.UserService;
import manito.server.util.HttpServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final HttpServletUtil httpServletUtil;

    //todo: 왜 403 forbidden 에러 뜨는지?
    /**
     * 유저정보 조회 API
     * @return
     */
    @PostMapping ("/info")
    public ResponseEntity info(HttpServletRequest requestHeader) {
        return new ResponseEntity<>(userService.getCurrentUserInfo(), HttpStatus.OK);
    }
}
