package hr.fer.rassus.lab.prvi.repository;

import hr.fer.rassus.lab.prvi.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
}
