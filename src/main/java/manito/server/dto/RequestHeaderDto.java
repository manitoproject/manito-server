package manito.server.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class RequestHeaderDto {
    private String authorization;

    @Builder
    public RequestHeaderDto(String authorization) {
        this.authorization = authorization;
    }
}
