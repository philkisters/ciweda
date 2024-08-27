package me.kisters.ciweda.db.entities;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sensorId;

    private String source;
    private String originalId;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point position;

    private String sensorType;

    @ElementCollection
    @CollectionTable(name = "sensor_measurement_types", joinColumns = @JoinColumn(name = "sensor_id"))
    @Column(name = "measurement_type")
    private List<MeasurementType> measurementTypes;

    private String additionalInformation;

    public Sensor(String source, String originalId, Point position, List<MeasurementType> measurementTypes, String additionalInformation) {
        this.source = source;
        this.originalId = originalId;
        this.position = position;
        this.measurementTypes = measurementTypes;
        this.additionalInformation = additionalInformation;
    }

    public Sensor() {
    }

    public String getSource() {
        return source;
    }

    public String getOriginalId() {
        return originalId;
    }

    public Long getId() {
        return sensorId;
    }

    public Point getPosition() {
        return position;
    }
}
