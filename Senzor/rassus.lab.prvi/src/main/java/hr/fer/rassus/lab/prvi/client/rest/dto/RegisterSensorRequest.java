package hr.fer.rassus.lab.prvi.client.rest.dto;

public record RegisterSensorRequest(Double longitude, Double latitude, String ip, Integer port) {
}
