package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.entity.PurposeRest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PurposeRestResponse {
    private int id;

    private String name;

    private String address;

    private String phoneNumber;

    private String subDescription;

    private int userId;

    public static PurposeRestResponse from(PurposeRest purposeRest) {
        return PurposeRestResponse.builder()
                .id(purposeRest.getId())
                .name(purposeRest.getName())
                .address(purposeRest.getAddress())
                .phoneNumber(purposeRest.getPhoneNumber())
                .subDescription(purposeRest.getSubDescription().getValue())
                .userId(purposeRest.getUser().getId())
                .build();
    }
}
