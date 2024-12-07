package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByUserId(int userId);
}
