package UOSense.UOSense_Backend.common.securityFilter;

import UOSense.UOSense_Backend.dto.LoginUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;


public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();


    public CustomAuthenticationFilter() {
        // url과 일치할 경우 해당 필터가 동작합니다.
        super(new AntPathRequestMatcher("/api/v1/user/signin"));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // 해당 요청이 POST 인지 확인
        if(!isPost(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        // POST 이면 body 를 LoginUser( 로그인 정보 DTO ) 에 매핑
        LoginUser loginUser = objectMapper.readValue(request.getReader(), LoginUser.class);

        // ID, PASSWORD가 있는지 확인
        if(!StringUtils.hasLength(loginUser.getEmail())
                || !StringUtils.hasLength(loginUser.getPassword())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        // 처음에는 인증 되지 않은 토큰 생성
        CustomAuthenticationToken token = new CustomAuthenticationToken(
                loginUser.getEmail(),
                loginUser.getPassword()
        );

        // Manager 에게 인증 처리
        return getAuthenticationManager().authenticate(token);
    }

    private boolean isPost(HttpServletRequest request) {

        return "POST".equals(request.getMethod());
    }

}