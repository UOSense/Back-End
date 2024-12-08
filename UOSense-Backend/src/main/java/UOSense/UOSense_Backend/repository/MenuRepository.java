package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.Menu;
import UOSense.UOSense_Backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findAllByRestaurantId(int restaurantId);

    @Query("SELECT m.restaurant FROM Menu m WHERE m.name LIKE %:keyword%")
    List<Restaurant> findByNameContains(@Param("keyword") String keyword);
}