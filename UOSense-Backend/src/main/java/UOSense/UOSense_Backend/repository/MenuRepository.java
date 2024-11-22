package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findAllByRestaurantId(int restaurantId);
}