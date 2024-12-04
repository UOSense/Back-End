package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.NewUserRequest;
import UOSense.UOSense_Backend.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private PasswordEncoder passwordEncoder;    // Spring Security 추가 필수
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
}
