package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByUserIdAndReviewId(int userId, int reviewId);
}