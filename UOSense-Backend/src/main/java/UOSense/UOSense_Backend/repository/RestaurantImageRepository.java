package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.Restaurant;
import UOSense.UOSense_Backend.entity.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Integer>{

    @Query("SELECT ri.imageUrl FROM RestaurantImage ri WHERE ri.restaurant = :restaurant ORDER BY ri.id ASC")
    String findFirstImageUrlByRestaurantOrderByIdAsc(@Param("restaurant") Restaurant restaurant);
}
