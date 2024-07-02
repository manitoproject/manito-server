package manito.server.controller;

import lombok.RequiredArgsConstructor;
import manito.server.service.KakaoOauthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login/oauth/test")
@RequiredArgsConstructor
public class OauthTestController {
    private final KakaoOauthService kakaoOauthService;

    @Value("${oauth.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${oauth.kakao.redirect-url}")
    private String REDIRECT_URL;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URL;

        model.addAttribute("location", location);

        return "login";
    }

    @GetMapping("/callback")
    @ResponseBody
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        System.out.println("code = " + code);
//        String accessToken = kakaoOauthService.getAccessTokenFromKakao(code);
//        KakaoUserInfoResponseDto userInfo = kakaoOauthService.getUserInfo(accessToken);

//        System.out.println("userInfo = " + userInfo);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
