package me.kisters.ciweda.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class CollectorStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;

    private int receivedSensorsCount;
    private int newSensorsCount;

    private int receivedMeasurementsCount;
    private int newMeasurementsCount;

    private LocalDateTime timestamp;

    public CollectorStatistics() {
        this.source = "unknown";
        receivedSensorsCount = 0;
        newSensorsCount = 0;
        receivedMeasurementsCount = 0;
        newMeasurementsCount = 0;
        timestamp = LocalDateTime.now();
    }

    public CollectorStatistics(final String source) {
        this.source = source;
        receivedSensorsCount = 0;
        newSensorsCount = 0;
        receivedMeasurementsCount = 0;
        newMeasurementsCount = 0;
        timestamp = LocalDateTime.now();
    }

    public void addReceivedSensorsCount(int receivedSensorsCount) {
        if (receivedSensorsCount > 0) {
            this.receivedSensorsCount += receivedSensorsCount;
        }
    }

    public void addNewSensorsCount(int newSensorsCount) {
        if (newSensorsCount > 0) {
            this.newSensorsCount += newSensorsCount;
        }
    }

    public void addReceivedMeasurementsCount(int receivedMeasurementsCount) {
        if (receivedMeasurementsCount > 0) {
            this.receivedMeasurementsCount += receivedMeasurementsCount;
        }
    }

    public void addNewMeasurementsCount(int newMeasurementsCount) {
        if (newMeasurementsCount > 0) {
            this.newMeasurementsCount += newMeasurementsCount;
        }
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public int getReceivedSensorsCount() {
        return receivedSensorsCount;
    }

    public int getNewSensorsCount() {
        return newSensorsCount;
    }

    public int getReceivedMeasurementsCount() {
        return receivedMeasurementsCount;
    }

    public int getNewMeasurementsCount() {
        return newMeasurementsCount;
    }

    @Override
    public String toString() {
        return "CollectorStatistics{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", receivedSensorsCount=" + receivedSensorsCount +
                ", newSensorsCount=" + newSensorsCount +
                ", receivedMeasurementsCount=" + receivedMeasurementsCount +
                ", newMeasurementsCount=" + newMeasurementsCount +
                ", timestamp=" + timestamp +
                '}';
    }
}
