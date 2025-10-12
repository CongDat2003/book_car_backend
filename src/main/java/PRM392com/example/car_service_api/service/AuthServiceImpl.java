package PRM392com.example.car_service_api.service;


import PRM392com.example.car_service_api.dto.AuthResponseDto;
import PRM392com.example.car_service_api.dto.LoginDto;
import PRM392com.example.car_service_api.dto.RegisterDto;
import PRM392com.example.car_service_api.Model.User;
import PRM392com.example.car_service_api.repository.UserRepository;
import PRM392com.example.car_service_api.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private EmailService emailService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    public User register(RegisterDto registerDto) {
        if (userRepository.existsByEmailOrPhoneNumber(registerDto.getEmail(), registerDto.getPhoneNumber())) {
            throw new RuntimeException("Email or Phone Number already exists!");
        }
        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        // Trả về cả token, userId và fullName
        return new AuthResponseDto(token, user.getId(), user.getFullName());
    }

    @Override
    public AuthResponseDto loginWithGoogle(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // 1. Tìm user trong DB, nếu chưa có thì tạo mới
                User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(name);
                    // Mật khẩu ngẫu nhiên vì người dùng đăng nhập qua Google
                    newUser.setPassword(passwordEncoder.encode("social-login-default-password"));
                    newUser.setRole("USER");
                    return userRepository.save(newUser);
                });

                // 2. Tạo đối tượng Authentication cho user (dù cũ hay mới)
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, Collections.singleton(new SimpleGrantedAuthority(user.getRole())));

                // 3. Tạo token của hệ thống
                String jwt = tokenProvider.generateToken(authentication);

                // 4. Trả về response hoàn chỉnh
                return new AuthResponseDto(jwt, user.getId(), user.getFullName());

            } else {
                throw new RuntimeException("Invalid Google ID token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google token verification failed: " + e.getMessage());
        }
    }


    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token có hiệu lực trong 1 giờ
        userRepository.save(user);

        emailService.sendPasswordResetToken(email, token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}