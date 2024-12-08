package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.enumClass.Category;
import UOSense.UOSense_Backend.common.enumClass.DoorType;
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
    void registerMenu(NewMenuRequest menuRequest, String imageUrl);
    void register(RestaurantRequest restaurantRequest);
    void edit(RestaurantRequest restaurantRequest);
    void delete(int restaurantId);
    BusinessDayList findBusinessDayList(int restaurantId);
    void editBusinessDay(BusinessDayList businessDayList);
    void saveBusinessDay(BusinessDayList businessDayList);
    void deleteBusinessDay(int businessDayId);
}