package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReviewInfo;
import UOSense.UOSense_Backend.dto.ReviewList;
import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.entity.ReviewLike;
import UOSense.UOSense_Backend.repository.RestaurantRepository;
import UOSense.UOSense_Backend.repository.ReviewLikeRepository;
import UOSense.UOSense_Backend.repository.ReviewRepository;
import UOSense.UOSense_Backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public ReviewList findListByRestaurantId(int restaurantId) {
        if (!restaurantRepository.existsById(restaurantId))
            throw new IllegalArgumentException("존재하지 않는 식당입니다.");

        List<Review> reviews = reviewRepository.findAllByRestaurantId(restaurantId);
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
    public void addLike(int userId,int reviewId) {
        try {
            // 1. 좋아요 이력 저장 시도
            reviewLikeRepository.save(new ReviewLike(
                    userRepository.getReferenceById(userId),    // 프록시 객체
                    reviewRepository.getReferenceById(reviewId) // 프록시 객체
            ));
        } catch (EntityNotFoundException e) {
            // 사용자가 존재하지 않는 경우
            throw new NoSuchElementException("사용자 혹은 리뷰를 찾을 수 없습니다.");
        } catch (DataIntegrityViolationException e) {
            // 이미 좋아요를 누른 경우 (unique constraint 위반)
            throw new IllegalStateException("이미 좋아요를 누른 리뷰입니다.");
        }

        // 2. 리뷰 좋아요 개수 증가
        int updatedRows = reviewRepository.increaseLikeCount(reviewId);
        if (updatedRows == 0) {
            throw new NoSuchElementException("해당 리뷰가 존재하지 않습니다.");
        }
    }
}
