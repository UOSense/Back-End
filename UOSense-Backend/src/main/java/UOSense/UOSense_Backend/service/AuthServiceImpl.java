package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    final int CODE_LENGTH = 6;
    private final RedisUtil redisUtil;
    Random random = new Random();

    @Override
    public String createAuthCode() {
        char[] code = new char[CODE_LENGTH];
        for (int i=0;i<CODE_LENGTH;i++) {
            int edge = random.nextInt(2);
            if (edge == 0) {
                code[i] = (char) random.nextInt(10);
                continue;
            }
            code[i] = (char) ('A' + random.nextInt(25));
        }
        return Arrays.toString(code);
    }

    @Override
    public void saveAuthCode(String email, String authCode ) {
        if (email == null || authCode == null)
            throw new IllegalArgumentException("메일 주소 혹은 인증코드가 비어있습니다.");
        try {
            redisUtil.setDataExpire(email, authCode);
        } catch (Exception e) {
            throw new IllegalArgumentException("인증코드 저장에 실패했습니다.");
        }
    }

    @Override
    public boolean checkAuthCode(String email, String authCode) {
        String redisAuthCode = redisUtil.getData(email);
        if (redisAuthCode == null) throw new NoSuchElementException("해당 메일로 보낸 인증코드가 존재하지 않습니다");
        return authCode.equals(redisAuthCode);
    }
}
