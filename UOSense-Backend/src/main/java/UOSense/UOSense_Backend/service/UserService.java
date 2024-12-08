package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.NewUserRequest;
import UOSense.UOSense_Backend.entity.User;

public interface UserService {
    User register(NewUserRequest newUserRequest);
    boolean checkPassword(String rawPassword, String encodedPassword);
}