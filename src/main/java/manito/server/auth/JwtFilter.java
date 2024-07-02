package manito.server.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import manito.server.entity.User;
import manito.server.exception.CustomException;
import manito.server.exception.ErrorCode;
import manito.server.service.JwtTokenService;
import manito.server.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    /**
     * 이 필터에서 엑세스 토큰이 유효한지 확인한 후 SecurityContext에 계정정보 저장
     * @param servletRequest  The request to process
     * @param servletResponse The response associated with the request
     * @param filterChain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                     to for further processing
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());
        String jwt = resolveToken(httpServletRequest);
        System.out.println("jwt = " + jwt);

        if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
            //토큰 Payload에 있는 userId 가져오기
            Long userId = Long.valueOf(jwtTokenService.getPayload(jwt));
            User user = userService.getUser(userId);
            if (user == null)
                throw new CustomException(ErrorCode.NOT_EXIST_USER);
            UserDetails userDetails = UserPrincipal.create(user);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 헤더에서 엑세스 토큰 가져오기
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return null;
    }
}
