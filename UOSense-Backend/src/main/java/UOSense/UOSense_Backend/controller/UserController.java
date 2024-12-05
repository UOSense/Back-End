package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "회원 관리")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final JWTUtil jwtUtil;

    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh;
        try {
            refresh = jwtUtil.parseCookie(request);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Map<String, String> tokens;
        try {
            tokens = jwtUtil.createTokens(refresh);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 응답
        response.setHeader("access", "Bearer " + tokens.get("access"));
        response.addCookie(jwtUtil.createCookie("refresh", tokens.get("refresh")));

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Reissue refresh token success");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 로그아웃 API, redis에 저장된 refresh token을 삭제
     */
    @PutMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        String refresh;
        try {
            refresh = jwtUtil.parseCookie(request);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.deleteRefresh(refresh);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("message", "Signed out successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}