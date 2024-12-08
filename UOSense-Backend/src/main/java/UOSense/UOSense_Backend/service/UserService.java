package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.NewUserRequest;

public interface UserService {
    void register(NewUserRequest newUserRequest);
    boolean checkNickName(String nickname);
    boolean checkPassword(String rawPassword, String encodedPassword);
}