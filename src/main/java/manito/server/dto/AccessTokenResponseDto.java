package manito.server.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessTokenResponseDto {
    private String accessToken;

    @Builder
    public AccessTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
