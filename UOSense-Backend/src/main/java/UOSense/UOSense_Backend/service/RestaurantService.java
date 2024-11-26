package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.Category;
import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.*;
import UOSense.UOSense_Backend.entity.Restaurant;

import java.util.List;

public interface RestaurantService {
    List<RestaurantListResponse> getAllRestaurants();
    List<RestaurantListResponse> getRestaurantsByFilter(DoorType doorType, Category category);
    List<RestaurantListResponse> getRestaurantsByCategory(Category category);
    List<RestaurantListResponse> getRestaurantsByDoorType(DoorType doorType);
    RestaurantInfo getRestaurantInfoById(int restaurantId);
    Restaurant getRestaurantById(int restaurantId);
    List<MenuResponse> findMenuBy(int restaurantId);
    void saveMenuWith(NewMenuRequest menuRequest, String imageUrl);
    void register(RestaurantRequest restaurantRequest);
    void edit(RestaurantRequest restaurantRequest);
    void delete(int restaurantId);
    BusinessDayList findBusinessDayListBy(int restaurantId);
    void editBusinessDayWith(BusinessDayList businessDayList);
    void saveBusinessDayWith(BusinessDayList businessDayList);
    void deleteBusinessDayWith(int businessDayId);
}