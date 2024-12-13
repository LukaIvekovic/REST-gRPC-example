package hr.fer.rassus.lab.prvi;

import hr.fer.rassus.lab.prvi.client.grpc.SensorGrpcClient;
import hr.fer.rassus.lab.prvi.client.rest.SensorRestService;
import hr.fer.rassus.lab.prvi.client.rest.dto.SaveReadingRequest;
import hr.fer.rassus.lab.prvi.server.SensorReadingService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SensorApplication {
	private static final Logger logger = Logger.getLogger(SensorApplication.class.getName());
	private static final int SERVER_PORT = 9090; // Za prave aplikacije, citati iz konfiguracijske datoteke
	private static final String SERVER_IP = "localhost"; // Za prave aplikacije, citati iz konfiguracijske datoteke

	public static void main(String[] args) throws IOException, InterruptedException {
		var readingService = new ReadingService();

		Server server = ServerBuilder.forPort(SERVER_PORT)
				.addService(new SensorReadingService(readingService))
				.build();

		server.start();
		logger.info("Server started, listening on: " + SERVER_IP + ":" + SERVER_PORT);

		Thread taskThread = new Thread(() -> {
			try {
				sensorWork(readingService);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "An error in the sensor occured, application is stopping! ", e);
				server.shutdown();
				System.exit(1);
			}
		});
		taskThread.start();

		server.awaitTermination();
	}

	private static void sensorWork(ReadingService readingService) {
		var sensorRestService = new SensorRestService(SERVER_IP, SERVER_PORT); // registering the sensor, exceptions aren't handled
		var neighborClient = new SensorGrpcClient(sensorRestService.getClosestNeighbor());

		while (true) {
			var currentReading = readingService.read();
			logger.info("Current reading: " + currentReading);

			var neighborReading = neighborClient.fetchNeighborReading();
			logger.info("Neighbor reading: " + neighborReading);

			var calibratedReading = calibrateReading(currentReading, neighborReading);
			logger.info("Calibrated reading: " + calibratedReading);

			sensorRestService.saveReading(
					new SaveReadingRequest(
							calibratedReading.temperature(),
							calibratedReading.humidity(),
							calibratedReading.pressure(),
							calibratedReading.co(),
							calibratedReading.no2(),
							calibratedReading.so2()
					)
			);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "An error occured while trying to sleep the thread! ", e);
			}
		}
	}

	private static SensorReading calibrateReading(SensorReading currentReading, SensorReading neighborReading) {
		Double temperature = calculateAverage(
				currentReading.temperature(), neighborReading != null ? neighborReading.temperature() : null);
		Double humidity = calculateAverage(
				currentReading.humidity(), neighborReading != null ? neighborReading.humidity() : null);
		Double pressure = calculateAverage(
				currentReading.pressure(), neighborReading != null ? neighborReading.pressure() : null);
		Double co = calculateAverage(
				currentReading.co(), neighborReading != null ? neighborReading.co() : null);
		Double no2 = calculateAverage(
				currentReading.no2(), neighborReading != null ? neighborReading.no2() : null);
		Double so2 = calculateAverage(
				currentReading.so2(), neighborReading != null ? neighborReading.so2() : null);

		return new SensorReading(temperature, humidity, pressure, co, no2, so2);
	}

	private static Double calculateAverage(Double sensorReading, Double neighborReading) {
		if (sensorReading == null || sensorReading == 0) {
			return neighborReading == 0 ? null : neighborReading;
		}

		if (neighborReading == null || neighborReading == 0) {
			return sensorReading;
		}

		return (sensorReading + neighborReading) / 2;
	}
}
