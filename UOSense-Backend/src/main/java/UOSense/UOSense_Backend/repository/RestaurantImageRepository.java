package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Integer>{

    @Query(value = "SELECT ri.image_url FROM restaurant_image ri WHERE ri.restaurant_id = :restaurantId ORDER BY ri.id ASC LIMIT 1", nativeQuery = true)
    Optional<String> findFirstImageUrl(@Param("restaurantId") int restaurantId);
    @Query(value = "SELECT ri.* " +
            "FROM restaurant_image ri " +
            "WHERE ri.restaurant_id in :restaurantIds " +
            "ORDER BY ri.id ASC LIMIT 1", nativeQuery = true)
    List<RestaurantImage> findAllFirstImageUrl(@Param("restaurantIds") List<Integer> restaurantIds);
    List<RestaurantImage> findAllByRestaurantId(int restaurantId);
}
