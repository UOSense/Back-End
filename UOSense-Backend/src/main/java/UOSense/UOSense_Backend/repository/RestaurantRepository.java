package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>{

}
