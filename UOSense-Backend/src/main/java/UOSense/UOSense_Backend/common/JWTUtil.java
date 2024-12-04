package UOSense.UOSense_Backend.common;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    // 암호화 알고리즘은 HS256을 사용
    public JWTUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * 사용자명 추출
     */
    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    /**
     * 권한 추출
     */
    public Role getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", Role.class);
    }

    /**
     * token 유효확인. true면 만료, false면 유효.
     */
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    /**
     * accessToken인지 refreshToken인지 확인
     */
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    /**
     * JWT 발급
     */
    public String createJwt(String category, String email, Role role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);     // 쿠키가 살아있을 시간
        //cookie.setSecure();          // https에서만 동작할것인지 (로컬은 http 환경이라 안먹음, 배포환경에서만 활성화)
        //cookie.setPath("/");        // 쿠키가 전역 경로에서 동작, 아니면 특정 경로에서만 동작하게 제한할 수 있음
        //cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js에서 접근할 수 없음)

        return cookie;
    }
}