package UOSense.UOSense_Backend.dto;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantInfo {
    private int id;

    private String name;

    private DoorType doorType;

    private double latitude;

    private double longitude;

    private String address;

    private String phoneNumber;

    private double rating;

    private Restaurant.Category category;

    private String subDescription;

    private String description;

    private int reviewCount;

    private int bookmarkCount;

    public static RestaurantInfo from(Restaurant restaurant) {
        return RestaurantInfo.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .doorType(restaurant.getDoorType())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .address(restaurant.getAddress())
                .phoneNumber(restaurant.getPhoneNumber())
                .rating(restaurant.getRating())
                .category(restaurant.getCategory())
                .subDescription(restaurant.getSubDescription())
                .description(restaurant.getDescription())
                .reviewCount(restaurant.getReviewCount())
                .bookmarkCount(restaurant.getBookmarkCount())
                .build();
    }
}
