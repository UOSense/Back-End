package UOSense.UOSense_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthCodeRequest {
    private String email;
    private String code;
}
