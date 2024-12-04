package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.dto.NewUserRequest;
import UOSense.UOSense_Backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 관리")
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-in")
    @Operation(summary = "회원가입", description = "회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 회원을 등록했습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "잘못된 요청입니다.")
    })
    /** 프론트에서 비밀번호와 비밀번호 확인 일치할 때만 요청 보냈다고 가정
     * 반환값 boolean과 String(응답 메시지) 중에 고민 중 프론트에게 물어보려 함
     */
    public ResponseEntity<Boolean> create (NewUserRequest newUserRequest) {
        try {
            userService.register(newUserRequest);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
