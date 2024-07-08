package manito.server.util;

import jakarta.servlet.http.HttpServletRequest;
import manito.server.dto.RequestHeaderDto;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpServletUtil {
    public RequestHeaderDto getRequestHeaderDto(HttpServletRequest request) {
        String authorizationTemp = (StringUtils.hasLength(request.getHeader("authorization"))) ? request.getHeader("authorization") : null;
        String authorization;

        if (null == authorizationTemp) {
            authorization = null;
        } else {
            String[] authorizationSplit = authorizationTemp.split(" ");
            if (authorizationSplit.length > 1) {
                authorization = authorizationSplit[1];
            } else {
                authorization = null;
            }
        }

        return RequestHeaderDto.builder()
                .authorization(authorization)
                .build();
    }
}
