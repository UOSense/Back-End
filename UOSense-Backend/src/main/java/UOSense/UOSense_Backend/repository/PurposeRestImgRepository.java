package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.PurposeRestImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeRestImgRepository extends JpaRepository<PurposeRestImg, Integer> {
    List<PurposeRestImg> findAllByRestaurantId(int restaurantId);
}