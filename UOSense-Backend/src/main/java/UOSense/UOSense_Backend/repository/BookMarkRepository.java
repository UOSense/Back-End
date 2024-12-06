package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.BookMark;
import UOSense.UOSense_Backend.entity.BusinessDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<BookMark, Integer> {
}
