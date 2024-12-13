package hr.fer.rassus.lab.prvi.client.rest;

import hr.fer.rassus.lab.prvi.client.rest.dto.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

public class SensorRestService {
    private static final Logger logger = Logger.getLogger(SensorRestService.class.getName());

    private static final String SERVER_URL = "http://localhost:8080/";
    private static final double MIN_LATITUDE = 45.75;
    private static final double MAX_LATITUDE = 45.85;
    private static final double MIN_LONGITUDE = 15.87;
    private static final double MAX_LONGITUDE = 16.0;

    private final Integer sensorId;
    private final SensorRestClient client;
    private final ClosestNeighbor closestNeighbor;

    public SensorRestService(String gRpcServerIp, Integer gRpcServerPort) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            client = retrofit.create(SensorRestClient.class);
            sensorId = registerSensor(gRpcServerIp, gRpcServerPort);

        } catch (Exception e) {
            logger.severe("Failed to create or register sensor on the server, application is stopping! Error: " + e.getMessage());
            throw new RuntimeException("Failed to create or register sensor on the server, application is stopping!", e);
        }

        // u konstruktoru se odmah dohvaća najbliži susjed, posto se ne mijenja tijekom rada aplikacije
        ClosestNeighbor closestNeighborTemp;
        try {
            var neighborResponse = fetchClosestNeighbor();
            closestNeighborTemp = new ClosestNeighbor(neighborResponse.ip(), neighborResponse.port());

            logger.info("Closest neighbor successfully fetched: " + closestNeighborTemp.ip() + ":" + closestNeighborTemp.port());

        } catch (Exception e) { // case kada mozda ne postoji najblizi susjed (npr. postoji samo jedan senzor na serveru)
            closestNeighborTemp = null;

            logger.warning("Failed to get closest neighbor from the server, readings won't be calibrated! Error: " + e.getMessage());
        }
        closestNeighbor = closestNeighborTemp;
    }

    public void saveReading(SaveReadingRequest saveReadingRequest) {
        try {
            Response<IdResponse> response = client.saveReading(sensorId, saveReadingRequest).execute();
            var body = response.body();

            // nije dovoljno provjeriti response.isSuccessful() jer prema uputama labosa moze biti i 204 No Content, makar nije spremljeno jer ne postoji senzor
            if (nonNull(body) && nonNull(body.id())) {
                logger.info("Reading successfully saved on the server!");
            } else {
                logger.warning("Failed to save reading on the server. Response code: " + response.code());
            }

        } catch (Exception e) {
            logger.warning("Error while saving reading on the server: " + e.getMessage());
        }
    }

    public ClosestNeighbor getClosestNeighbor() {
        return closestNeighbor;
    }

    private NeighborResponse fetchClosestNeighbor() {
        try {
            var closestNeighbor = client.getClosestNeighbor(sensorId).execute().body();

            if (nonNull(closestNeighbor) && nonNull(closestNeighbor.ip()) && nonNull(closestNeighbor.port())) {
                return closestNeighbor;
            } else {
                throw new RuntimeException("Failed to get closest neighbor from the server!");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get latest reading from closest neighbor!", e);
        }
    }

    private Integer registerSensor(String gRpcServerIp, Integer gRpcServerPort) throws IOException {
        double latitude = MIN_LATITUDE + Math.random() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + Math.random() * (MAX_LONGITUDE - MIN_LONGITUDE);

        RegisterSensorRequest request = new RegisterSensorRequest(longitude, latitude, gRpcServerIp, gRpcServerPort);

        Call<IdResponse> call = client.registerSensor(request);
        Response<IdResponse> response = call.execute();

        if (response.isSuccessful()) {
            if (nonNull(response.body()) && nonNull(response.body().id())) {
                return response.body().id();
            } else {
                throw new RuntimeException("Failed to register sensor on the server. Response body or id gotten is null!");
            }
        } else {
            throw new RuntimeException("Failed to register sensor on the server. Response code: " + response.code());
        }
    }
}
