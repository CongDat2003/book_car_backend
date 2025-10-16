package PRM392com.example.car_service_api.repository;

import PRM392com.example.car_service_api.Model.VehicleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleModelRepository extends JpaRepository<VehicleModel, Integer> {
    List<VehicleModel> findByBrandId(Integer brandId);
}