package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReviewList;
import UOSense.UOSense_Backend.dto.ReviewRequest;
import UOSense.UOSense_Backend.dto.ReviewResponse;
import UOSense.UOSense_Backend.entity.Review;
import java.util.List;

public interface ReviewService {
    ReviewList findListByRestaurantId(int restaurantId);
    void delete(int reviewId);
    void addLike(int userId, int reviewId);
    int register(ReviewRequest reviewRequest, int userId);
    Review find(int id);
    List<ReviewResponse> findByUserId(int userId);
}
