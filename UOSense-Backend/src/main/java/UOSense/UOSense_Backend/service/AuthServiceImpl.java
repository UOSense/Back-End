package UOSense.UOSense_Backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    final int CODE_LENGTH = 6;

    @Override
    public char[] createAuthCode() {
        char[] code = new char[CODE_LENGTH];
        for (int i=0;i<CODE_LENGTH;i++) {
            code[i] = (char) ('0' + (int)(Math.random() * 10));
        }
        return code;
    }

    @Override
    public void saveAuthCode(String authCode) {

    }

    @Override
    public boolean checkAuthCode(String codeToCheck) {
        return false;
    }
}
