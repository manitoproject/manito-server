package manito.server.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.auth.SecurityUtil;
import manito.server.dto.PaperDto;
import manito.server.dto.RequestHeaderDto;
import manito.server.dto.ResponseDto;
import manito.server.entity.Paper;
import manito.server.entity.User;
import manito.server.repository.PaperRepository;
import manito.server.util.AppUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaperService {
    private final UserService userService;
    private final PaperRepository paperRepository;

    public ResponseDto<?> create(RequestHeaderDto requestHeader, PaperDto requestBody) {
        log.info("{}", requestBody);

        try {
            User user = userService.getUser(SecurityUtil.getCurrentUserId());

            Paper paper = Paper.builder()
                    .user(user)
                    .category(requestBody.getCategory())
                    .title(requestBody.getTitle())
                    .theme(requestBody.getTheme())
                    .regDateTime(LocalDateTime.now())
                    .build();

            paperRepository.saveAndFlush(paper);

        } catch (Exception e) {
            log.error("{}|PaperService.create|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }
}
