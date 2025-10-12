package PRM392com.example.car_service_api.controller;



import PRM392com.example.car_service_api.Model.User;
import PRM392com.example.car_service_api.dto.UpdateUserDto;
import PRM392com.example.car_service_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(null); // Không bao giờ trả về mật khẩu
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserDetails(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(updateUserDto.getFullName());
            user.setPhoneNumber(updateUserDto.getPhoneNumber());
            user.setDateOfBirth(updateUserDto.getDateOfBirth());
            user.setGender(updateUserDto.getGender());
            user.setAddress(updateUserDto.getAddress());

            User updatedUser = userRepository.save(user);
            updatedUser.setPassword(null);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

}
