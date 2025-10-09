package PRM392com.example.car_service_api.controller;
import PRM392com.example.car_service_api.dto.AuthResponseDto;
import PRM392com.example.car_service_api.dto.LoginDto;
import PRM392com.example.car_service_api.dto.RegisterDto;
import PRM392com.example.car_service_api.Model.User;
import PRM392com.example.car_service_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Tất cả API trong controller này sẽ có tiền tố /api/auth

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register") // URL: POST http://localhost:8080/api/auth/register
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        try {
            User registeredUser = authService.register(registerDto);
            // Che mật khẩu trước khi trả về
            registeredUser.setPassword(null);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        AuthResponseDto authResponse = authService.login(loginDto);
        return ResponseEntity.ok(authResponse);
    }
}

