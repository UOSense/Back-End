package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.common.Utils.RedisUtilForToken;
import UOSense.UOSense_Backend.common.enumClass.Role;
import UOSense.UOSense_Backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import UOSense.UOSense_Backend.common.Utils.JWTUtil;
import UOSense.UOSense_Backend.dto.NewUserRequest;
import UOSense.UOSense_Backend.service.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관리")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService; 
    private final JWTUtil jwtUtil;
    private final RedisUtilForToken redisUtilForToken;
    @Value("${jwt.access.duration}")
    private long accessDuration;
    @Value("${jwt.refresh.duration}")
    private long refreshDuration;
  
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

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 회원을 등록했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    /** 프론트에서 비밀번호와 비밀번호 확인 일치할 때만 요청 보냈다고 가정 */
    public ResponseEntity<Boolean> signUp(@RequestBody NewUserRequest newUserRequest, HttpServletResponse response) {
        User user;
        try {
             user = userService.register(newUserRequest);
        } catch (IllegalArgumentException | DuplicateRequestException e) {
            return ResponseEntity.badRequest().body(false);
        }
        String username = user.getEmail();
        Role role = user.getRole();

        // accessToken과 refreshToken 생성
        String accessToken = jwtUtil.createJwt("access", username, role, accessDuration);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, refreshDuration);

        // redis에 insert (key = username / value = refreshToken)
        redisUtilForToken.setToken(username, refreshToken, Duration.ofMillis(refreshDuration));

        // 응답
        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 응답 타입 설정

        return ResponseEntity.ok().body(true);
    }
}
