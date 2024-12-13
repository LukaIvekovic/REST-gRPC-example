package hr.fer.rassus.lab.prvi.client.rest.dto;

public record SaveReadingRequest(
        Double temperature,
        Double humidity,
        Double pressure,
        Double co,
        Double no2,
        Double so2
) {
}
