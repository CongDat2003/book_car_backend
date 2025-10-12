package PRM392com.example.car_service_api.repository;

import PRM392com.example.car_service_api.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Tự động tạo câu lệnh "SELECT * FROM users WHERE email = ?"
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
}
