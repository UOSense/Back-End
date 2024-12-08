package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.CustomUserDetails;
import UOSense.UOSense_Backend.dto.NewUserRequest;
import UOSense.UOSense_Backend.repository.UserRepository;
import UOSense.UOSense_Backend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid authentication!");
        }

        return new CustomUserDetails(user);
    }
    
    @Override
    public void register(NewUserRequest newUserRequest) {
        // 요청 정보 검증
        if (!validatedUserInfo(newUserRequest))
            throw new IllegalArgumentException("유효하지 않은 사용자 정보입니다.");
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(newUserRequest.getPassword());
        // DB에 저장
        userRepository.save(newUserRequest.toEntity(encodedPassword));
    }

    private boolean validatedUserInfo(NewUserRequest newUserRequest) {
        // 웹메일 검증
        boolean validatedMail = mailService.checkDuplicatedMail(newUserRequest.getEmail()) &&
                mailService.checkMailAddress(newUserRequest.getEmail());
        // 비밀번호 검증
        boolean validatedPassword = checkPasswordFormat(newUserRequest.getPassword());
        // 닉네임 검증
        boolean validatedNickname = checkNickName(newUserRequest.getNickname());
        return validatedMail || validatedPassword || validatedNickname;
    }

    private boolean checkPasswordFormat(String password) {
        if (password.length() >= 8 && password.length() <= 20)
            throw new IllegalArgumentException("비밀번호 자리수 제한");

        // 각 조건을 확인하기 위한 플래그
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        // 문자열의 각 문자를 확인
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowerCase = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if ("!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) != -1) {
                hasSpecialChar = true;
            }
            if (hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar) return true;
        }
        throw new IllegalArgumentException("비밀번호 형식 제한");
    }

    private boolean checkNickName(String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new DuplicateRequestException("이미 존재하는 닉네임입니다.");
        return true;
    }

    /** 비밀번호 일치 여부를 확인합니다 */
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
