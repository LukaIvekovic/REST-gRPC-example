package hr.fer.rassus.lab.prvi.mapper;

import hr.fer.rassus.lab.prvi.dto.SensorReadingSaveRequest;
import hr.fer.rassus.lab.prvi.dto.SensorRegistrationRequest;
import hr.fer.rassus.lab.prvi.dto.SensorResponse;
import hr.fer.rassus.lab.prvi.dto.SensorReadingResponse;
import hr.fer.rassus.lab.prvi.model.Sensor;
import hr.fer.rassus.lab.prvi.model.SensorReading;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface SensorMapper {
    List<SensorResponse> toSensorResponseList(List<Sensor> sensors);

    List<SensorReadingResponse> toSensorReadingResponseList(List<SensorReading> sensorReadings);

    @Mapping(target = "sensorId", source = "sensorReading.sensor.id")
    SensorReadingResponse toSensorReadingResponse(SensorReading sensorReading);

    SensorResponse toSensorResponse(Sensor sensor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensorReadings", ignore = true)
    Sensor toSensor(SensorRegistrationRequest sensorRegistrationRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    SensorReading toSensorReading(SensorReadingSaveRequest sensorReadingSaveRequest);
}
