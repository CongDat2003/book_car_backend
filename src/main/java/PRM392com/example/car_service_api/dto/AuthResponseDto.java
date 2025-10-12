package PRM392com.example.car_service_api.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private Long userId;
    private String fullName; // Thêm trường này

    public AuthResponseDto(String token, Long userId, String fullName) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
    }
}
