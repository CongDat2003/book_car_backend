package PRM392com.example.car_service_api.service;
import PRM392com.example.car_service_api.dto.AuthResponseDto;
import PRM392com.example.car_service_api.dto.LoginDto;

import PRM392com.example.car_service_api.dto.RegisterDto;
import PRM392com.example.car_service_api.Model.User;

// trong file AuthService.java
public interface AuthService {
    User register(RegisterDto registerDto);
    AuthResponseDto login(LoginDto loginDto); // Sửa kiểu trả về từ String
}
