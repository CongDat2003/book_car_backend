package PRM392com.example.car_service_api.service;
import PRM392com.example.car_service_api.dto.AuthResponseDto;
import PRM392com.example.car_service_api.dto.LoginDto;
import PRM392com.example.car_service_api.dto.RegisterDto;
import PRM392com.example.car_service_api.Model.User;
import PRM392com.example.car_service_api.repository.UserRepository;
import PRM392com.example.car_service_api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public User register(RegisterDto registerDto) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setRole("USER"); // Mặc định vai trò là USER

        return userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo token
        String token = tokenProvider.generateToken(authentication);

        // Tìm user trong DB để lấy ID
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        // Trả về đối tượng AuthResponseDto chứa cả token và userId
        return new AuthResponseDto(token, user.getId());
    }
}