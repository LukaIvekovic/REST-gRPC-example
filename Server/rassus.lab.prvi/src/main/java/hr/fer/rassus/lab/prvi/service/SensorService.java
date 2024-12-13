package hr.fer.rassus.lab.prvi.service;

import hr.fer.rassus.lab.prvi.exception.BadRequestException;
import hr.fer.rassus.lab.prvi.exception.NotFoundException;
import hr.fer.rassus.lab.prvi.model.Sensor;
import hr.fer.rassus.lab.prvi.model.SensorReading;
import hr.fer.rassus.lab.prvi.repository.SensorReadingRepository;
import hr.fer.rassus.lab.prvi.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {
    private final static double EARTH_RADIUS = 6371.0;
    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;

    public Integer registerSensor(Sensor sensor) {
        log.debug("Saving sensor to database");

        return sensorRepository.save(sensor).getId();
    }

    public List<Sensor> fetchAllSensors() {
        log.debug("Fetching all sensors");

        return sensorRepository.findAll();
    }

    public Sensor fetchClosestNeighbor(Integer id) {
        log.debug("Fetching closest neighbor for sensor with id: {}", id);

        var sensor = sensorRepository.findById(id).orElseThrow(() -> new NotFoundException("Sensor with id " + id + " not found!"));
        var neighbors = sensorRepository.findAll();

        return neighbors.stream()
                .filter(s -> !s.equals(sensor))
                .min((s1, s2) -> {
                    var distance1 = calculateDistance(sensor, s1);
                    var distance2 = calculateDistance(sensor, s2);

                    return Double.compare(distance1, distance2);
                })
                .orElseThrow(() -> new BadRequestException("Sensor with id " + id + " doesn't have any neighbors!"));
    }

    public Integer addSensorReading(Integer sensorId, SensorReading sensorReading) {
        log.debug("Saving sensor reading to database");

        var sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new NotFoundException("Sensor with id " + sensorId + " not found!"));

        sensor.getSensorReadings().add(sensorReading);
        sensorReading.setSensor(sensor);

        return sensorReadingRepository.save(sensorReading).getId();
    }

    public List<SensorReading> fetchSensorReadings(Integer sensorId) {
        log.debug("Fetching sensor readings for sensor with id: {}", sensorId);

        var sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new NotFoundException("Sensor with id " + sensorId + " not found!"));

        return sensor.getSensorReadings();
    }

    private double calculateDistance(Sensor sensor1, Sensor sensor2) {
        var lat1 = Math.toRadians(sensor1.getLatitude());
        var lon1 = Math.toRadians(sensor1.getLongitude());
        var lat2 = Math.toRadians(sensor2.getLatitude());
        var lon2 = Math.toRadians(sensor2.getLongitude());

        var dlat = lat2 - lat1;
        var dlon = lon2 - lon1;

        var a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
