package hr.fer.rassus.lab.prvi.dto;

public record SensorReadingSaveRequest(
        Double temperature,
        Double humidity,
        Double pressure,
        Double co,
        Double no2,
        Double so2
) {
}
