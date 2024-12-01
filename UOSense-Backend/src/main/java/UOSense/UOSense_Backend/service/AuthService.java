package UOSense.UOSense_Backend.service;

public interface AuthService {
    char[] createAuthCode();
    void saveAuthCode(String authCode);
    boolean checkAuthCode(String codeToCheck);
}
