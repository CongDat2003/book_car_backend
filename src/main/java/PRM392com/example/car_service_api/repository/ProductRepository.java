package PRM392com.example.car_service_api.repository;

import PRM392com.example.car_service_api.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByServiceId(Long serviceId);
}
