package PRM392com.example.car_service_api.Model;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String phoneNumber;

    private String role;
    // Thêm 2 trường này vào cuối lớp User.java
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private int rewardPoints;
    private String membershipTier;
    private LocalDate membershipExpiry;


}
