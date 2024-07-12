package manito.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.auth.SecurityUtil;
import manito.server.dto.PaperDto;
import manito.server.dto.PaperListDto;
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
        log.info("{}|PaperService.create|requestBody = {}", SecurityUtil.getCurrentUserId(), requestBody);

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

    public ResponseDto<?> readList(RequestHeaderDto requestHeader, Long userId) {
        log.info("{}|PaperService.readList", SecurityUtil.getCurrentUserId());
        PaperListDto response = new PaperListDto();

        try {
            User user = userService.getUser(userId);
            List<Paper> paperList = paperRepository.findByUser(user);
            List<PaperDto> paperDtoList = new ArrayList<>();
            for (Paper paper : paperList) {
                PaperDto paperDto = PaperDto.builder()
                        .id(paper.getId())
                        .category(paper.getCategory())
                        .title(paper.getTitle())
                        .theme(paper.getTheme())
                        .regDateTime(paper.getRegDateTime())
                        .modDateTime(paper.getModDateTime())
                        .build();

                paperDtoList.add(paperDto);
            }

            response.setData(paperDtoList);
        } catch (Exception e) {
            log.error("{}|PaperService.readList|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .data(response)
                .build();
    }

    public ResponseDto<?> update(RequestHeaderDto requestHeader, PaperDto requestBody) {
        log.info("{}|PaperService.update|requestBody = {}", SecurityUtil.getCurrentUserId(), requestBody);

        try {
            Optional<Paper> optionalPaper = paperRepository.findById(requestBody.getId());
            if (optionalPaper.isEmpty())
                throw new NullPointerException();

            Paper paper = optionalPaper.get();

            paper.update(requestBody.getCategory(), requestBody.getTitle(), requestBody.getTheme(), LocalDateTime.now());

            paperRepository.saveAndFlush(paper);
        } catch (Exception e) {
            log.error("{}|PaperService.update|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
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
