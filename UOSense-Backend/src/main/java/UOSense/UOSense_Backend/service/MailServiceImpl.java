package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MailServiceImpl implements MailService{
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Override
    public boolean checkMailAddress(String mailAddress) {
        if(!mailAddress.endsWith("@uos.ac.kr"))
            throw new IllegalArgumentException("올바른 웹메일 형식이 아닙니다.");
        return true;
    }

    @Override
    public boolean checkDuplicatedMail(String mailAddress) {
        if (userRepository.existsByEmail(mailAddress)) {
            throw new IllegalArgumentException("이미 가입된 메일 주소입니다.");
        }
        return true;
    }

    @Override
    public MimeMessage createMail(String email, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("[시대생 맛집 지도] 회원가입 인증 번호");
        helper.setText("<html><body style=\"text-align: center;\"><h2> 인증번호 [ "+authCode+" ]</h2>" +
                "<b>시대생 맛집 지도 회원가입 화면에서 인증번호를 입력해 주세요.</b></body></html>", true);
        /*실제로 존재하는 주소. 인증 이메일 주소와 달라도 됨. SMTP 서버 기본 주소 존재 시 없어도 됨*/
        // helper.setFrom("contact@project.com");  
        return message;
    }
    @Override
    public void sendMail(MimeMessage message) {
        mailSender.send(message);
    }
}
