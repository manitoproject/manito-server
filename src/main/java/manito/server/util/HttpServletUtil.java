package manito.server.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import manito.server.dto.RequestHeaderDto;
import manito.server.exception.CustomException;
import manito.server.exception.ErrorCode;
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

    public String getRreshToken(HttpServletRequest request) {
        Cookie[] list = request.getCookies();
        if (list == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(
                Collectors.toList()).get(0);

        if (refreshTokenCookie == null)
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        return refreshTokenCookie.getValue();
    }
}
