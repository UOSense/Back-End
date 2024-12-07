package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.BusinessDayInfo;
import UOSense.UOSense_Backend.dto.ReviewInfo;
import UOSense.UOSense_Backend.dto.ReviewList;
import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewList findListByRestaurantId(int restaurantId) {
        List<Review> reviews = reviewRepository.findAllByRestaurantId(restaurantId);
        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("해당 식당의 리뷰가 존재하지 않습니다.");
        }
        List<ReviewInfo> reviewInfoList = reviews.stream()
                .map(ReviewInfo::from)
                .toList();
        return new ReviewList(restaurantId,reviewInfoList);
    }

    @Override
    public void delete(int reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("삭제할 리뷰가 존재하지 않습니다.");
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public void addLike(int reviewId) {
        int updatedRows = reviewRepository.increaseLikeCount(reviewId);
        if (updatedRows == 0) {
            throw new NoSuchElementException("해당 리뷰가 존재하지 않습니다.");
        }
    }
}
