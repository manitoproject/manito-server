package manito.server.controller;

import lombok.RequiredArgsConstructor;
import manito.server.dto.NicknameRequestDto;
import manito.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /**
     * 유저정보 조회 API
     * @return
     */
    @PostMapping("/info")
    public ResponseEntity<?> info() {
        return new ResponseEntity<>(userService.getCurrentUserInfo(), HttpStatus.OK);
    }

    @PostMapping("/nickname")
    public ResponseEntity<?> nickname(@RequestBody NicknameRequestDto requestBody) {
        return new ResponseEntity<>(userService.changeNickname(requestBody), HttpStatus.OK);
    }
}
