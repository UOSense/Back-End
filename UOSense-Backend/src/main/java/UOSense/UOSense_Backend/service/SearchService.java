package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.dto.RestaurantListResponse;
import UOSense.UOSense_Backend.entity.Restaurant;

import java.util.List;

public interface SearchService {
    enum sortFilter { DEFAULT, BOOKMARK, DISTANCE, RATING, REVIEW, PRICE }
    /**
     * 입력어와 출입구문 필터를 바탕으로 식당을 검색합니다.
     */
    List<RestaurantListResponse> search(String keyword, DoorType doorType);
    List<Restaurant> sort(List<Restaurant> result, sortFilter filter);
}
