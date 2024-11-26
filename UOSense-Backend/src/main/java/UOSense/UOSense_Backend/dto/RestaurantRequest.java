package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.common.Category;
import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.common.SubDescription;
import UOSense.UOSense_Backend.entity.Restaurant;
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

    public static Restaurant toEntity(RestaurantRequest restaurantRequest, double rating, int reviewCount, int bookmarkCount) {
        if (restaurantRequest.getId() == -1) {
            // createRestaurant
            return Restaurant.builder()
                    .name(restaurantRequest.getName())
                    .doorType(restaurantRequest.getDoorType())
                    .longitude(restaurantRequest.getLongitude())
                    .latitude(restaurantRequest.getLatitude())
                    .address(restaurantRequest.getAddress())
                    .phoneNumber(restaurantRequest.getPhoneNumber())
                    .rating(rating)
                    .category(restaurantRequest.getCategory())
                    .subDescription(restaurantRequest.getSubDescription())
                    .description(restaurantRequest.getDescription())
                    .reviewCount(reviewCount)
                    .bookmarkCount(bookmarkCount)
                    .build();
        }
        else {
            // editRestaurant
            return Restaurant.builder()
                    .id(restaurantRequest.getId())
                    .name(restaurantRequest.getName())
                    .doorType(restaurantRequest.getDoorType())
                    .longitude(restaurantRequest.getLongitude())
                    .latitude(restaurantRequest.getLatitude())
                    .address(restaurantRequest.getAddress())
                    .phoneNumber(restaurantRequest.getPhoneNumber())
                    .rating(rating)
                    .category(restaurantRequest.getCategory())
                    .subDescription(restaurantRequest.getSubDescription())
                    .description(restaurantRequest.getDescription())
                    .reviewCount(reviewCount)
                    .bookmarkCount(bookmarkCount)
                    .build();
        }
    }
}
