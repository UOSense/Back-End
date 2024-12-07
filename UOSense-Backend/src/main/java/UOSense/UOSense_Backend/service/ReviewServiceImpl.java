package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReviewRequest;
import UOSense.UOSense_Backend.dto.ReviewResponse;
import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.entity.User;
import UOSense.UOSense_Backend.repository.RestaurantRepository;
import UOSense.UOSense_Backend.repository.ReviewRepository;
import UOSense.UOSense_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public int register(ReviewRequest reviewRequest, int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("사용자 정보가 없습니다.");
        }
        Optional<Restaurant> restaurant = restaurantRepository.findById(reviewRequest.getRestaurantId());
        if (restaurant.isEmpty()) {
            throw new IllegalArgumentException("식당 정보가 없습니다.");
        }

        Review review = Review.toEntity(reviewRequest, user.get(), restaurant.get());

        review = reviewRepository.save(review);
        return review.getId();
    }

    @Override
    public Review find(int reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            throw new IllegalArgumentException("리뷰 정보가 없습니다.");
        }
        return review.get();
    }

    // restaurantId로 리뷰 목록 조회와 헷갈릴 우려가 있어 ByUserId를 추가함
    @Override
    public List<ReviewResponse> findByUserId(int userId) {
        List<Review> reviewList = reviewRepository.findAllByUserId(userId);
        if (reviewList.isEmpty()) {
            throw new IllegalArgumentException("해당 사용자의 리뷰 정보가 없습니다.");
        }
        return reviewList.stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
