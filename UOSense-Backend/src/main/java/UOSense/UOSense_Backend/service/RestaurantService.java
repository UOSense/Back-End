package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.MenuResponse;

import java.util.List;

public interface RestaurantService {
    List<MenuResponse> findMenuBy(int restaurantId);
}