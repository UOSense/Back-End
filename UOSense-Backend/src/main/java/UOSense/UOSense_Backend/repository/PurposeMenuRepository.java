package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.PurposeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurposeMenuRepository extends JpaRepository<PurposeMenu, Integer> {
}
