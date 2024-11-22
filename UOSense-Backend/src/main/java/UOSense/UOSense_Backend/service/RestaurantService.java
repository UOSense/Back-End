package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.dto.RestaurantResponse;
import UOSense.UOSense_Backend.entity.Restaurant;

import java.util.List;

public interface RestaurantService {
    public List<RestaurantListResponse> getAllRestaurants();
    public List<RestaurantListResponse> getRestaurantsByFilter(DoorType doorType, Restaurant.Category category);
    public List<RestaurantListResponse> getRestaurantsByCategory(Restaurant.Category category);
    public List<RestaurantListResponse> getRestaurantsByDoorType(DoorType doorType);
    public RestaurantResponse getRestaurantById(int RestaurantId);

}
