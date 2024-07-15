package manito.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.auth.SecurityUtil;
import manito.server.dto.MessageDto;
import manito.server.dto.RequestHeaderDto;
import manito.server.dto.ResponseDto;
import manito.server.entity.Message;
import manito.server.entity.Paper;
import manito.server.entity.User;
import manito.server.repository.MessageRepository;
import manito.server.repository.PaperRepository;
import manito.server.util.AppUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final PaperRepository paperRepository;

    public ResponseDto<?> create(RequestHeaderDto requestHeader, MessageDto requestBody) {
        log.info("{}|MessageService.create|requestBody = {}", SecurityUtil.getCurrentUserId(), requestBody);

        try {
            User user = userService.getUser(SecurityUtil.getCurrentUserId());
            Optional<Paper> optionalPaper = paperRepository.findById(requestBody.getPaperId());
            if (optionalPaper.isEmpty())
                throw new RuntimeException();
            Paper paper = optionalPaper.get();

            Message message = Message.builder()
                    .paper(paper)
                    .user(user)
                    .theme(requestBody.getTheme())
                    .content(requestBody.getContent())
                    .regDateTime(LocalDateTime.now())
                    .font(requestBody.getFont())
                    .fontColor(requestBody.getFontColor())
                    .isPublic(requestBody.getIsPublic())
                    .build();

            messageRepository.saveAndFlush(message);

        } catch (Exception e) {
            log.error("{}|MessageService.create|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }

    public ResponseDto<?> read(RequestHeaderDto requestHeader) {
        log.info("{}|MessageService.read", SecurityUtil.getCurrentUserId());
        List<MessageDto> messageDtoList = new ArrayList<>();

        try {
            User user = userService.getUser(SecurityUtil.getCurrentUserId());

            List<Message> messageList = messageRepository.findByUser(user);
            for (Message message : messageList) {
                MessageDto messageDto = MessageDto.builder()
                        .id(message.getId())
                        .paperId(message.getPaper().getId())
                        .userId(message.getUser().getId())
                        .theme(message.getTheme())
                        .content(message.getContent())
                        .regDateTime(message.getRegDateTime())
                        .modDateTime(message.getModDateTime())
                        .font(message.getFont())
                        .fontColor(message.getFontColor())
                        .isPublic(message.getIsPublic())
                        .build();

                messageDtoList.add(messageDto);
            }
        } catch (Exception e) {
            log.error("{}|MessageService.read|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .data(messageDtoList)
                .build();
    }
}
