package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.dto.ReviewInfo;
import UOSense.UOSense_Backend.dto.ReviewList;
import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.entity.ReviewLike;
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

    @Override
    @Transactional(readOnly = true)
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
    public void addLike(int userId,int reviewId) {
        // 1. 리뷰 존재 확인 및 좋아요 증가를 한번에 처리
        int updatedRows = reviewRepository.increaseLikeCount(reviewId);
        if (updatedRows == 0) {
            throw new NoSuchElementException("해당 리뷰가 존재하지 않습니다.");
        }

        try {
            // 2. 좋아요 이력 저장 시도
            reviewLikeRepository.save(new ReviewLike(
                    userRepository.getReferenceById(userId),    // 프록시 객체
                    reviewRepository.getReferenceById(reviewId) // 프록시 객체
            ));
        } catch (EntityNotFoundException e) {
            // 3. 사용자가 존재하지 않는 경우
            throw new NoSuchElementException("사용자를 찾을 수 없습니다.");
        } catch (DataIntegrityViolationException e) {
            // 4. 이미 좋아요를 누른 경우 (unique constraint 위반)
            throw new IllegalStateException("이미 좋아요를 누른 리뷰입니다.");
        }
    }
}
