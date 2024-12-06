package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReviewList;

public interface ReviewService {
    ReviewList findListByRestaurantId(int restaurantId);
    void delete(int reviewId);
}
