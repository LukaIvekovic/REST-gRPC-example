package hr.fer.rassus.lab.prvi.server;

import hr.fer.rassus.lab.prvi.ReadingService;
import hr.fer.rassus.lab.prvi.sensor.Reading.Empty;
import hr.fer.rassus.lab.prvi.sensor.Reading.SensorReading;
import hr.fer.rassus.lab.prvi.sensor.SensorReadingServiceGrpc.SensorReadingServiceImplBase;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class SensorReadingService extends SensorReadingServiceImplBase {
    private static final Logger logger = Logger.getLogger(SensorReadingService.class.getName());

    private final ReadingService readingService;

    public SensorReadingService(ReadingService readingService) {
        super();
        this.readingService = readingService;
    }

    @Override
    public void fetchSensorReading(Empty request, StreamObserver<SensorReading> responseObserver) {
        logger.info("Received request for sensor reading");

        var reading = readingService.read();

        var sensorReading = SensorReading.newBuilder()
                .setTemperature(checkNull(reading.temperature()))
                .setHumidity(checkNull(reading.humidity()))
                .setPressure(checkNull(reading.pressure()))
                .setCo(checkNull(reading.co()))
                .setNo2(checkNull(reading.no2()))
                .setSo2(checkNull(reading.so2()))
                .build();

        responseObserver.onNext(sensorReading);
        responseObserver.onCompleted();
    }

    // jer je server napravljen da vraca primitive, ali u kalibraciji se nula tretira isto kao i null
    private double checkNull(Double value) {
        return value == null ? 0.0 : value;
    }
}
