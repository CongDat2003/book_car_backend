package PRM392com.example.car_service_api.repository;

import PRM392com.example.car_service_api.Model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
