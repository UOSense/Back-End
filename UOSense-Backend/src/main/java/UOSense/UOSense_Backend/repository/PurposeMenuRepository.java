package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.PurposeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeMenuRepository extends JpaRepository<PurposeMenu, Integer> {
    @Query(value = "SELECT m.image_url " +
            "FROM Purpose_Menu m " +
            "WHERE m.restaurant_id = :restaurantId " , nativeQuery = true)
    List<String> findImageUrls(@Param("restaurantId") int restaurantId);
}