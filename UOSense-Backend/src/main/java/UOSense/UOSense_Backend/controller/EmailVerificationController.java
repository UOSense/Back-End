package UOSense.UOSense_Backend.controller;

import UOSense.UOSense_Backend.dto.AuthCodeRequest;
import UOSense.UOSense_Backend.dto.WebmailRequest;
import UOSense.UOSense_Backend.service.AuthService;
import UOSense.UOSense_Backend.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "웹메일 인증 관리")
@RestController
@RequestMapping("/api/v1/webmail")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final AuthService authService;
    private final MailService mailService;

    @GetMapping("/check-format")
    public ResponseEntity<String> validateWebMail(String mailAddress) {
        try {
            if (mailService.checkMailAddress(mailAddress) && mailService.checkDuplicatedMail(mailAddress))
                return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> sendAuthCode(@RequestBody WebmailRequest webmailRequest) {
        try {
            String email = webmailRequest.getEmail();
            String purpose = webmailRequest.getPurpose();
            String code = authService.createAuthCode();
            mailService.sendAuthMail(email, purpose, code);
            authService.saveAuthCode(email, code);
            return ResponseEntity.status(HttpStatus.OK).body("인증 코드가 전송되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드 발급에 실패하였습니다. "+e.getMessage());
        }
    }
    @PostMapping("/authenticate-code")
    public ResponseEntity<String> validateCode(@RequestBody AuthCodeRequest authCodeRequest) {
        try {
            if (authService.checkAuthCode(authCodeRequest.getEmail(),authCodeRequest.getCode()))
                return ResponseEntity.status(HttpStatus.OK).body("인증에 성공했습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}