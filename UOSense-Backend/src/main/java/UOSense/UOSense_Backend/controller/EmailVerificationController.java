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

    /** 웹메일 중복 확인 클릭 시 */
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

    /** 인증번호 발송 클릭 시 */
    @PostMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Boolean> sendAuthCode(@RequestBody WebmailRequest webmailRequest) {
        try {
            String email = webmailRequest.getEmail();
            String purpose = webmailRequest.getPurpose();
            String code = authService.createAuthCode();
            mailService.sendAuthMail(email, purpose, code);
            authService.saveAuthCode(email, code);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

    /** 인증번호 확인 클릭 시 */
    @PostMapping(value = "/authenticate-code", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Boolean> validateCode(@RequestBody AuthCodeRequest authCodeRequest) {
        try {
            if (authService.checkAuthCode(authCodeRequest.getEmail(),authCodeRequest.getCode()))
                return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }
}
