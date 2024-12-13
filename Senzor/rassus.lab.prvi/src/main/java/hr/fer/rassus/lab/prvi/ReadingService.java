package hr.fer.rassus.lab.prvi;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadingService {
    private static final String CSV_FILE_PATH = "src/main/resources/readings.csv";

    private final List<List<String>> readings;
    private final Long startTime;

    public ReadingService() {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception occured while trying to read readings from a file! ", e);
        }

        this.readings = records;
        this.startTime = System.currentTimeMillis();
    }

    public SensorReading read() {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;

        int index = (int) (elapsedSeconds % 100) + 1;
        List<String> randomReading = readings.get(index);

        return new SensorReading(
                getDoubleValue(randomReading.get(0)),
                getDoubleValue(randomReading.get(1)),
                getDoubleValue(randomReading.get(2)),
                getDoubleValue(randomReading.get(3)),
                getDoubleValue(randomReading.get(4)),
                getDoubleValue(randomReading.get(5))
        );
    }

    private Double getDoubleValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return null;
        }
    }
}
