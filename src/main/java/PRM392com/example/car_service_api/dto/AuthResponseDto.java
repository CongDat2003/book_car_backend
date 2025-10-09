package PRM392com.example.car_service_api.dto;

import lombok.Data;

@Data // Lombok tự tạo getter, setter
public class AuthResponseDto {
    private String token;
    private Long userId;

    public AuthResponseDto(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }
}
