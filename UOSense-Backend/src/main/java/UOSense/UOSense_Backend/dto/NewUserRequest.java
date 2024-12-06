package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NewUserRequest {
    private String email;

    private String password;

    private String nickname;

    public User toEntity(String encodedPw) {
        return User.builder()
                .email(this.getEmail())
                .password(encodedPw)
                .nickname(this.getNickname())
                .role(User.Role.USER)
                .build();
    }
}
