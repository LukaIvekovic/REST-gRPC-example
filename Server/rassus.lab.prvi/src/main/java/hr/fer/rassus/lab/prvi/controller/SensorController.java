package hr.fer.rassus.lab.prvi.controller;

import hr.fer.rassus.lab.prvi.dto.*;
import hr.fer.rassus.lab.prvi.exception.NotFoundException;
import hr.fer.rassus.lab.prvi.mapper.SensorMapper;
import hr.fer.rassus.lab.prvi.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * S obzirom da nema velik broj metoda za ovu vjezbu ostavio sam sve u jednom controlleru,
 * inače bolje razdvojiti u vise kontrollera, npr. jos jedan SensorReadingController sa
 * base mappingom /sensors/{id}/readings (isto tako i za servis i za mappera)
 */
@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
@Slf4j
public class SensorController {
    private final SensorService sensorService;
    private final SensorMapper sensorMapper;

    @PostMapping
    public ResponseEntity<IdResponse> registerSensor(@RequestBody SensorRegistrationRequest sensorRegistrationRequest) {
        log.info("Registering sensor");

        var sensorId = sensorService.registerSensor(sensorMapper.toSensor(sensorRegistrationRequest));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensorId)
                .toUri();

        return ResponseEntity.created(location).body(new IdResponse(sensorId));
    }

    @GetMapping
    public ResponseEntity<List<SensorResponse>> fetchAllSensors() {
        log.info("Fetching all sensors");

        var sensors = sensorService.fetchAllSensors();

        return ResponseEntity.ok(sensorMapper.toSensorResponseList(sensors));
    }

    @GetMapping("/{id}/neighbors/closest")
    public ResponseEntity<SensorResponse> fetchClosestNeighbor(@PathVariable Integer id) {
        log.info("Fetching closest neighbor for sensor with id: {}", id);

        var closestNeighbor = sensorService.fetchClosestNeighbor(id);

        return ResponseEntity.ok(sensorMapper.toSensorResponse(closestNeighbor));
    }

    @PostMapping("/{id}/readings")
    public ResponseEntity<IdResponse> addSensorReading(@PathVariable("id") Integer sensorId, @RequestBody SensorReadingSaveRequest sensorReadingSaveRequest) {
        log.info("Adding sensor reading for sensor with id: {}", sensorId);

        try {
            var sensorReadingId = sensorService.addSensorReading(sensorId, sensorMapper.toSensorReading(sensorReadingSaveRequest));

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(sensorReadingId)
                    .toUri();

            return ResponseEntity.created(location).body(new IdResponse(sensorReadingId));

        } catch (NotFoundException notFoundException) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}/readings")
    public ResponseEntity<List<SensorReadingResponse>> fetchSensorReadings(@PathVariable("id") Integer sensorId) {
        log.info("Fetching sensor readings for sensor with id: {}", sensorId);

        try {
            var sensorReadings = sensorService.fetchSensorReadings(sensorId);

            return ResponseEntity.ok(sensorMapper.toSensorReadingResponseList(sensorReadings));

        } catch (NotFoundException notFoundException) {
            return ResponseEntity.noContent().build();
        }
    }
}






















