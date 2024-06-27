package manito.server.auth;

import manito.server.util.TimeoutType;
import manito.server.util.WebClientUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class OAuthService {
    private final WebClientUtil webClientUtil;

    public OAuthService(WebClientUtil webClientUtil) {
        this.webClientUtil = webClientUtil;
    }

    public String getKakaoAccessToken(String code) {
/*        Object response;

        response = webClientUtil.getWebClient(TimeoutType.MS_10000)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(IN_HOST[finalHostIndex])
                        .port(IN_PORT)
                        .path(url)
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(request)))
                .retrieve()
                .bodyToMono(INResponseDto.class)
                .block();*/
    }
}
