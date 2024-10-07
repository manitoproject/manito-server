package manito.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manito.server.auth.SecurityUtil;
import manito.server.dto.MessageCountDto;
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
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final PaperRepository paperRepository;

    public ResponseDto<?> create(RequestHeaderDto requestHeader, MessageDto requestBody) {
        log.info("{}|MessageService.create|requestBody = {}", SecurityUtil.getCurrentUserId(), requestBody);

        try {
            if (requestBody.getIsPublic().equals("N") && requestBody.getAnonymous() == null)
                return ResponseDto.builder()
                        .result(AppUtil.RESULT_FAIL)
                        .description(AppUtil.ANONYMOUS_IS_NULL)
                        .build();

            Optional<Paper> optionalPaper = paperRepository.findById(requestBody.getPaperId());
            if (optionalPaper.isEmpty())
                return ResponseDto.builder()
                        .result(AppUtil.RESULT_FAIL)
                        .description(AppUtil.PAPER_IS_NULL)
                        .build();

            Paper paper = optionalPaper.get();
            Optional<Message> optionalMessage = messageRepository.findByPaperAndPosition(paper, requestBody.getPosition());
            if (optionalMessage.isPresent())
                return ResponseDto.builder()
                        .result(AppUtil.RESULT_FAIL)
                        .description(AppUtil.POSITION_IS_NOT_AVAILABLE)
                        .build();

            User user = userService.getUser(SecurityUtil.getCurrentUserId());

            Message message = Message.builder()
                    .paper(paper)
                    .user(user)
                    .theme(requestBody.getTheme())
                    .content(requestBody.getContent())
                    .regDateTime(LocalDateTime.now())
                    .font(requestBody.getFont())
                    .fontColor(requestBody.getFontColor())
                    .isPublic(requestBody.getIsPublic())
                    .anonymous(requestBody.getAnonymous())
                    .position(requestBody.getPosition())
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
                        .user(message.getUser())
                        .theme(message.getTheme())
                        .content(message.getContent())
                        .regDateTime(message.getRegDateTime())
                        .modDateTime(message.getModDateTime())
                        .font(message.getFont())
                        .fontColor(message.getFontColor())
                        .isPublic(message.getIsPublic())
                        .anonymous(message.getAnonymous())
                        .position(message.getPosition())
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

    public ResponseDto<?> readByPaper(RequestHeaderDto requestHeader, Long paperId) {
        log.info("MessageService.read|requestBody: {}", paperId);
        List<MessageDto> messageDtoList = new ArrayList<>();

        try {
            Optional<Paper> optionalPaper = paperRepository.findById(paperId);
            Paper paper = optionalPaper.orElseGet(null);

            List<Message> messageList = messageRepository.findByPaper(paper);
            for (Message message : messageList) {
                MessageDto messageDto = new MessageDto();

                User userId = User.builder()
                        .id(message.getUser().getId())
                        .build();

                User userIdAndNickname = User.builder()
                        .id(message.getUser().getId())
                        .nickname(message.getUser().getNickname())
                        .build();

                if (message.getIsPublic().equals("Y")) {
                    messageDto = MessageDto.builder()
                            .id(message.getId())
                            .paperId(message.getPaper().getId())
                            .user(userIdAndNickname)
                            .theme(message.getTheme())
                            .content(message.getContent())
                            .regDateTime(message.getRegDateTime())
                            .modDateTime(message.getModDateTime())
                            .font(message.getFont())
                            .fontColor(message.getFontColor())
                            .isPublic(message.getIsPublic())
//                            .anonymous(message.getAnonymous())
                            .position(message.getPosition())
                            .build();
                }

                if (message.getIsPublic().equals("N")) {
                    messageDto = MessageDto.builder()
                            .id(message.getId())
                            .paperId(message.getPaper().getId())
                            .user(userId)
                            .theme(message.getTheme())
                            .content(message.getContent())
                            .regDateTime(message.getRegDateTime())
                            .modDateTime(message.getModDateTime())
                            .font(message.getFont())
                            .fontColor(message.getFontColor())
                            .isPublic(message.getIsPublic())
                            .anonymous(message.getAnonymous())
                            .position(message.getPosition())
                            .build();
                }

                messageDtoList.add(messageDto);
            }
        } catch (Exception e) {
            log.error("MessageService.read|requsetBody = {}|error = {}", paperId, e.getMessage(), e);
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

    public ResponseDto<?> countMessages(RequestHeaderDto requestHeader, Long paperId) {
        log.info("MessageService.countMessages|requestBody: {}", paperId);
        MessageCountDto messageCount;

        try {
            Optional<Paper> optionalPaper = paperRepository.findById(paperId);
            Paper paper;
            if (optionalPaper.isPresent()) paper = optionalPaper.get();
            else throw new Exception("paper not found");

            List<Message> messageList = messageRepository.findByPaper(paper);

            messageCount = MessageCountDto.builder()
                    .count(messageList.size())
                    .build();
        } catch (Exception e) {
            log.error("MessageService.countMessages|error = {}", e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .data(messageCount)
                .build();
    }

    public ResponseDto<?> update(RequestHeaderDto requestHeader, MessageDto requestBody) {
        log.info("{}|MessageService.update|requestBody = {}", SecurityUtil.getCurrentUserId(), requestBody);

        try {
            //익명 여부는 수정불가
            /*if (requestBody.getIsPublic().equals("N") && requestBody.getAnonymous() == null)
                return ResponseDto.builder()
                        .result(AppUtil.RESULT_FAIL)
                        .description(AppUtil.ANONYMOUS_IS_NULL)
                        .build();*/

            Optional<Message> optionalMessage = messageRepository.findById(requestBody.getId());
            if (optionalMessage.isEmpty())
                throw new NullPointerException();
            Message message = optionalMessage.get();

            message.update(requestBody.getContent(), LocalDateTime.now(), requestBody.getFont(), requestBody.getFontColor());

            messageRepository.saveAndFlush(message);
        } catch (Exception e) {
            log.error("{}|MessageService.update|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
            return ResponseDto.builder()
                    .result(AppUtil.RESULT_FAIL)
                    .description(e.getMessage())
                    .build();
        }

        return ResponseDto.builder()
                .result(AppUtil.RESULT_SUCCESS)
                .build();
    }

    public ResponseDto<?> delete(RequestHeaderDto requestHeader, MessageDto requestBody) {
        log.info("{}|MessageService.delete|requestBody = {}", SecurityUtil.getCurrentUserId(), requestBody);

        try {
            messageRepository.deleteById(requestBody.getId());
        } catch (Exception e) {
            log.error("{}|MessageService.delete|error = {}", SecurityUtil.getCurrentUserId(), e.getMessage(), e);
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
