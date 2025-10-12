package PRM392com.example.car_service_api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateUserDto {
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
}
