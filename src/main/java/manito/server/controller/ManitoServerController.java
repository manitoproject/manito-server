package manito.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManitoServerController {
    @GetMapping
    public String main() {
        return "/main";
    }
}
