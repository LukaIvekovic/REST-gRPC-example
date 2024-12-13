package hr.fer.rassus.lab.prvi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "sensor_reading")
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "pressure")
    private Double pressure;

    @Column(name = "co")
    private Double co;

    @Column(name = "no2")
    private Double no2;

    @Column(name = "so2")
    private Double so2;

    @CreatedDate
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
