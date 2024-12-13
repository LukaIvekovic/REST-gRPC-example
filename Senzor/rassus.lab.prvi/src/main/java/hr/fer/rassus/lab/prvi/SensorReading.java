package hr.fer.rassus.lab.prvi;

public record SensorReading(
        Double temperature,
        Double humidity,
        Double pressure,
        Double co,
        Double no2,
        Double so2
) {
}
