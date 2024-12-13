package hr.fer.rassus.lab.prvi.dto;

import java.time.LocalDateTime;

public record SensorReadingResponse(
        Integer id,
        Integer sensorId,
        Double temperature,
        Double humidity,
        Double pressure,
        Double co,
        Double no2,
        Double so2,
        LocalDateTime timestamp
) {
}
