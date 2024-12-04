package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.JWTUtil;
import UOSense.UOSense_Backend.common.RedisUtilForToken;
import UOSense.UOSense_Backend.common.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "회원 관리")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final JWTUtil jwtUtil;
    private final RedisUtilForToken redisUtilForToken;
    @Value("${jwt.access.duration}")
    private long accessDuration;
    @Value("${jwt.refresh.duration}")
    private long refreshDuration;

    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 쿠키에 존재하는 refreshToken을 가져오자
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Cookie is null");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        for(Cookie cookie : cookies) {
            if("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        // 검증 시작
        // refreshToken이 없는 경우
        if(refresh == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Refresh token is null");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 유효기간 확인
        // 토큰이 refresh인지 확인
        String category;
        try {
            category = jwtUtil.getCategory(refresh);
        } catch (ExpiredJwtException e) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Refresh token is expired");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if(!category.equals("refresh")) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Invalid refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 새로운 Token을 만들기 위해 준비
        String email = jwtUtil.getEmail(refresh);
        Role role = jwtUtil.getRole(refresh);

        // Redis내에 존재하는 refreshToken인지 확인
        if(!redisUtilForToken.checkExistsToken(email)) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "No exists in redis refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        // 새로운 JWT Token 생성
        String newAccessToken = jwtUtil.createJwt("access", email, role, accessDuration);
        String newRefreshToken = jwtUtil.createJwt("refresh", email, role, refreshDuration);

        // update refreshToken to Redis
        redisUtilForToken.setToken(email, newRefreshToken, Duration.ofMillis(refreshDuration));

        // 응답
        response.setHeader("access", "Bearer " + newAccessToken);
        response.addCookie(jwtUtil.createCookie("refresh", newRefreshToken));

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Reissue refresh token success");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 로그아웃 API, redis에 저장된 refresh token을 삭제
     */
    @PutMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Cookie is null");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        for(Cookie cookie : cookies) {
            if("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        if(refresh == null) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Refresh token is null");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        String category;
        try {
            category = jwtUtil.getCategory(refresh);
        } catch (ExpiredJwtException e) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Refresh token expired");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        if(!category.equals("refresh")) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "Invalid refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        String key = jwtUtil.getEmail(refresh);

        if(!redisUtilForToken.checkExistsToken(key)) {
            Map<String, Object> message = new HashMap<>();
            message.put("message", "No exists in redis refresh token");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        redisUtilForToken.deleteToken(key);

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Signed out successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}