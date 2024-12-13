package hr.fer.rassus.lab.prvi.repository;

import hr.fer.rassus.lab.prvi.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Integer> {
}
