package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.entity.Restaurant;

import java.util.List;

public interface SearchService {
    List<RestaurantListResponse> search(String keyword, DoorType doorType);
}
