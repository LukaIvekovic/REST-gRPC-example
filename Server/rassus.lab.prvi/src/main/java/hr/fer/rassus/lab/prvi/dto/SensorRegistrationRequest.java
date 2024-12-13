package hr.fer.rassus.lab.prvi.dto;

public record SensorRegistrationRequest(
        Double latitude,
        Double longitude,
        String ip,
        Integer port
) {
}
