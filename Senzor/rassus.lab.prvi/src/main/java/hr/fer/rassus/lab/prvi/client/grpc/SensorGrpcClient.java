package hr.fer.rassus.lab.prvi.client.grpc;

import hr.fer.rassus.lab.prvi.SensorReading;
import hr.fer.rassus.lab.prvi.client.rest.dto.ClosestNeighbor;
import hr.fer.rassus.lab.prvi.sensor.Reading;
import hr.fer.rassus.lab.prvi.sensor.SensorReadingServiceGrpc;
import hr.fer.rassus.lab.prvi.sensor.SensorReadingServiceGrpc.SensorReadingServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Logger;

public class SensorGrpcClient {
    private static final Logger logger = Logger.getLogger(SensorGrpcClient.class.getName());

    private final SensorReadingServiceBlockingStub client;

    public SensorGrpcClient(ClosestNeighbor closestNeighbor) {
        SensorReadingServiceBlockingStub client;

        if (closestNeighbor == null) {
            logger.warning("Closest neighbor is null, can't connect to the server!");
            client = null;
        } else {
            try {
                ManagedChannel channel = ManagedChannelBuilder.forAddress(closestNeighbor.ip(), closestNeighbor.port())
                        .usePlaintext()
                        .build();

                client = SensorReadingServiceGrpc.newBlockingStub(channel);

            } catch (Exception e) {
                logger.warning("Failed to connect to the closest neighbor!");
                client = null;
            }
        }

        this.client = client;
    }

    public SensorReading fetchNeighborReading() {
        if (client == null) {
            return null;
        }

        try {
            var reading = client.fetchSensorReading(Reading.Empty.newBuilder().build());

            return new SensorReading(
                    reading.getTemperature(),
                    reading.getHumidity(),
                    reading.getPressure(),
                    reading.getCo(),
                    reading.getNo2(),
                    reading.getSo2()
            );

        } catch (Exception e) {
            logger.warning("Error occured while fetching readings from the closest neighbor!");
            return null;
        }
    }
}
