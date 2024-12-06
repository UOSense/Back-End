package UOSense.UOSense_Backend.service;

import UOSense.UOSense_Backend.entity.Review;
import UOSense.UOSense_Backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;

    @Override
    public void delete(int reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("삭제할 리뷰가 존재하지 않습니다.");
        }
        reviewRepository.deleteById(reviewId);
    }
}
