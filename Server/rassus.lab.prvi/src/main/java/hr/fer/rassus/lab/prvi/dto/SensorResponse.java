package hr.fer.rassus.lab.prvi.dto;

public record SensorResponse(
        Integer id,
        Double latitude,
        Double longitude,
        String ip,
        Integer port
) {
}
