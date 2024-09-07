package dev.milgodyn.carservice.persistence.repository;

import dev.milgodyn.carservice.persistence.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarEntity, String> {
}
