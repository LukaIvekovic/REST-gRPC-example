package hr.fer.rassus.lab.prvi.client.rest.dto;

public record NeighborResponse(
        Integer id,
        Double latitude,
        Double longitude,
        String ip,
        Integer port
) {
}
