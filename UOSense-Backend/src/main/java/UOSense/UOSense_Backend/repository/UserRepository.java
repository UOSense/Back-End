package UOSense.UOSense_Backend.repository;

import UOSense.UOSense_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    /**
     * 해당 이메일 주소로 가입된 유저가 있는지 확인합니다.
     */
    Boolean existsByEmail(String email);

    User findByEmail(String email);
}