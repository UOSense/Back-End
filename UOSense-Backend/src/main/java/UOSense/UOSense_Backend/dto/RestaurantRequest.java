package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.common.Category;
import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.common.SubDescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantRequest {
    private int id;

    private String name;

    private DoorType doorType;

    private double latitude;

    private double longitude;

    private String address;

    private String phoneNumber;

    private Category category;

    private SubDescription subDescription;

    private String description;
}
