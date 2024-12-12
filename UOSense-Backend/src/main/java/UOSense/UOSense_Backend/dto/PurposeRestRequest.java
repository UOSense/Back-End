package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.common.enumClass.SubDescription;
import UOSense.UOSense_Backend.entity.PurposeRest;
import UOSense.UOSense_Backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurposeRestRequest {
    private String name;

    private String address;

    private String phoneNumber;

    private SubDescription subDescription;

    private int userId;

    public static PurposeRest toEntity(PurposeRestRequest purposeRestRequest, User user) {
        return PurposeRest.builder()
                .name(purposeRestRequest.getName())
                .address(purposeRestRequest.getAddress())
                .phoneNumber(purposeRestRequest.getPhoneNumber())
                .subDescription(purposeRestRequest.getSubDescription())
                .user(user)
                .build();
    }
}
