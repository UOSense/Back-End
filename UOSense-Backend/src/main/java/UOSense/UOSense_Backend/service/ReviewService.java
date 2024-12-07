package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReviewRequest;
import UOSense.UOSense_Backend.dto.ReviewResponse;
import UOSense.UOSense_Backend.entity.Review;

import java.util.List;

public interface ReviewService {
    int register(ReviewRequest reviewRequest, int userId);
    ReviewResponse find(int reviewId);
    List<ReviewResponse> findByUserId(int userId);
}
