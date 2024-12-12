package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.PurposeDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeDayRepository extends JpaRepository<PurposeDay, Integer> {
    @Query(value = "SELECT b.* " +
            "FROM Purpose_BusinessDay b " +
            "WHERE b.restaurant_id = :restaurantId " , nativeQuery = true)
    List<PurposeDay> findAllByRestaurantId(@Param("restaurantId") int restaurantId);
}