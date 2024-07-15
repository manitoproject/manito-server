package manito.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import manito.server.dto.MessageDto;
import manito.server.service.MessageService;
import manito.server.util.HttpServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody MessageDto requestBody) {
        return new ResponseEntity<>(messageService.create(HttpServletUtil.getRequestHeaderDto(request), requestBody), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> read(HttpServletRequest request) {
        return new ResponseEntity<>(messageService.read(HttpServletUtil.getRequestHeaderDto(request)), HttpStatus.OK);
    }
}
