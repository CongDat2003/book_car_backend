package PRM392com.example.car_service_api.controller;
import PRM392com.example.car_service_api.dto.AuthResponseDto;
import PRM392com.example.car_service_api.dto.LoginDto;
import PRM392com.example.car_service_api.dto.RegisterDto;
import PRM392com.example.car_service_api.Model.User;
import PRM392com.example.car_service_api.dto.ResetPasswordDto;
import PRM392com.example.car_service_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Tất cả API trong controller này sẽ có tiền tố /api/auth

public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register") // URL: POST http://localhost:8080/api/auth/register
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
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
    public ResponseEntity<AuthResponseDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        AuthResponseDto authResponse = authService.login(loginDto);
        return ResponseEntity.ok(authResponse);
    }
    // ...
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> emailMap) {
        try {
            authService.forgotPassword(emailMap.get("email"));
            return ResponseEntity.ok(Map.of("message", "Password reset token has been sent to your email."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Tạo một DTO mới cho reset password
// (tạo file dto/ResetPasswordDto.java với 2 trường token và newPassword)
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetDto) {
        try {
            authService.resetPassword(resetDto.getToken(), resetDto.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password has been reset successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/google")
    public ResponseEntity<AuthResponseDto> authenticateWithGoogle(@RequestBody Map<String, String> tokenMap) {
        String idToken = tokenMap.get("idToken");
        AuthResponseDto authResponse = authService.loginWithGoogle(idToken);
        return ResponseEntity.ok(authResponse);
    }
}

