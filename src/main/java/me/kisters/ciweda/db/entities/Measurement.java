package me.kisters.ciweda.db.entities;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long measurementId;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    private LocalDateTime timestamp;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point position;

    private MeasurementType measurementType;
    private Double value;
    private String unit;

    public Measurement(LocalDateTime timestamp, Point position, MeasurementType measurementType, Double value, String unit) {
        this.timestamp = timestamp;
        this.position = position;
        this.measurementType = measurementType;
        this.value = value;
        this.unit = unit;
    }

    public Measurement() {
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public double getValue() {
        return value;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public long getSensorId() {
        return sensor.getId();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getId() {
        return measurementId;
    }

    public Sensor getSensor() {
        return sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return Objects.equals(sensor, that.sensor) && Objects.equals(timestamp, that.timestamp) && measurementType == that.measurementType && Objects.equals(value, that.value) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensor, timestamp, measurementType, value, unit);
    }
}
