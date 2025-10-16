package PRM392com.example.car_service_api.controller;

import PRM392com.example.car_service_api.Model.Brand;
import PRM392com.example.car_service_api.Model.Product;
import PRM392com.example.car_service_api.dto.ProductDto;
import PRM392com.example.car_service_api.Model.ServiceEntity;
import PRM392com.example.car_service_api.Model.VehicleModel;
import PRM392com.example.car_service_api.repository.BrandRepository;
import PRM392com.example.car_service_api.repository.ProductRepository;
import PRM392com.example.car_service_api.repository.ServiceRepository;
import PRM392com.example.car_service_api.repository.VehicleModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    @Autowired private BrandRepository brandRepository;
    @Autowired private VehicleModelRepository vehicleModelRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private ProductRepository productRepository;

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getBrands() {
        return ResponseEntity.ok(brandRepository.findAll());
    }

    @GetMapping("/brands/{brandId}/models")
    public ResponseEntity<List<VehicleModel>> getModelsByBrand(@PathVariable Integer brandId) {
        return ResponseEntity.ok(vehicleModelRepository.findByBrandId(brandId));
    }

    @GetMapping("/services")
    public ResponseEntity<List<ServiceEntity>> getServices() {
        return ResponseEntity.ok(serviceRepository.findAll());
    }

    @GetMapping("/services/{serviceId}/products")
    public ResponseEntity<List<ProductDto>> getProductsByService(@PathVariable Long serviceId) {
        List<Product> products = productRepository.findByServiceId(serviceId);
        List<ProductDto> result = products.stream().map(p -> {
            ProductDto dto = new ProductDto();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            dto.setImageUrl(p.getImageUrl());
            dto.setBrand(p.getBrand());
            return dto;
        }).toList();
        return ResponseEntity.ok(result);
    }
}
