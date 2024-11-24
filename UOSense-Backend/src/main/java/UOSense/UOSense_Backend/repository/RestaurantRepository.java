package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.common.Category;
import UOSense.UOSense_Backend.common.DoorType;
import UOSense.UOSense_Backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{
    List<Restaurant> findByDoorTypeAndCategory(DoorType doorType, Category category);
    List<Restaurant> findByCategory(Category category);
    List<Restaurant> findByDoorType(DoorType doorType);
}
