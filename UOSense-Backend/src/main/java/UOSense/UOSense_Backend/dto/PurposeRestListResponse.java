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
public class PurposeRestListResponse {
    private int id;

    private String name;

    private String address;

    private String phoneNumber;

    private String subDescription;

    private String restaurantImage;

    private int userId;

    public static PurposeRestListResponse from(PurposeRest purposeRest, String imageUrl) {
        return PurposeRestListResponse.builder()
                .id(purposeRest.getId())
                .name(purposeRest.getName())
                .address(purposeRest.getAddress())
                .phoneNumber(purposeRest.getPhoneNumber())
                .subDescription(purposeRest.getSubDescription().getValue())
                .restaurantImage(imageUrl)
                .userId(purposeRest.getUser().getId())
                .build();
    }
}
