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
    public void saveAuthCode(String authCode) {

    }

    @Override
    public boolean checkAuthCode(String codeToCheck) {
        return false;
    }
}
