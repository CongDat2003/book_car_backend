package PRM392com.example.car_service_api.Model;

import jakarta.persistence.*;
import PRM392com.example.car_service_api.Model.ServiceEntity;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String brand;
    private int stockQuantity;
}
