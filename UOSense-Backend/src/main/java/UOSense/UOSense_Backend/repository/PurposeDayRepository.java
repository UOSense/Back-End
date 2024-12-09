package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.PurposeDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeDayRepository extends JpaRepository<PurposeDay, Integer> {
//    List<PurposeDay> findAllByRestaurantId(int restaurantId);
}