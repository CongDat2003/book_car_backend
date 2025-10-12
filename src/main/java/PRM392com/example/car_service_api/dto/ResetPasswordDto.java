package PRM392com.example.car_service_api.dto;

import lombok.Data;

@Data // Tự động tạo getters (getToken, getNewPassword)
public class ResetPasswordDto {
    private String token;
    private String newPassword;
}
