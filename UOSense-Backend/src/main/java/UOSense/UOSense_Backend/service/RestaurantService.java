package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.MenuResponse;
import UOSense.UOSense_Backend.dto.NewMenuRequest;

import java.util.List;

public interface RestaurantService {
    List<MenuResponse> findMenuBy(int restaurantId);
    void saveMenuWith(NewMenuRequest menuRequest, String imageUrl);
}