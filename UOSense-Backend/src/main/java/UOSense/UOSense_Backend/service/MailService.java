package UOSense.UOSense_Backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {
    /**
     * 메일 주소 형식이 올바른 형식(@uos.ac.kr로 끝나는 형식)인지 검사합니다.
     */
    boolean checkMailAddress(String mailAddress);
    /**
     * 중복되는 이메일 주소인지 검사합니다.
     */
    boolean checkDuplicatedMail(String mailAddress);
    /**
     * 인증 코드를 보낼 메일을 생성합니다.
     */
    MimeMessage createMail(String email, String authCode) throws MessagingException;
    /**
     * 메일을 전송합니다.
     */
    void sendMail(MimeMessage message);
}
