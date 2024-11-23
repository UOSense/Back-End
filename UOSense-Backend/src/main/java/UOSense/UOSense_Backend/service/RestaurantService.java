package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantInfo;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.dto.MenuResponse;
import UOSense.UOSense_Backend.dto.NewMenuRequest;
import UOSense.UOSense_Backend.entity.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<RestaurantListResponse> getAllRestaurants();
    List<RestaurantListResponse> getRestaurantsByFilter(DoorType doorType, Restaurant.Category category);
    List<RestaurantListResponse> getRestaurantsByCategory(Restaurant.Category category);
    List<RestaurantListResponse> getRestaurantsByDoorType(DoorType doorType);
    RestaurantInfo getRestaurantById(int RestaurantId);
    List<MenuResponse> findMenuBy(int restaurantId);
    void saveMenuWith(NewMenuRequest menuRequest, String imageUrl);
}