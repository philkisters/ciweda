package me.kisters.ciweda.db.repositories;

import me.kisters.ciweda.db.entities.Measurement;
import me.kisters.ciweda.db.entities.MeasurementType;
import me.kisters.ciweda.db.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findMeasurementsBySensorAndMeasurementTypeAndTimestamp(Sensor sensor, MeasurementType measurementType, LocalDateTime timestamp);

    @Query(value = "SELECT DISTINCT ON (sensor_id, measurement_type) * FROM Measurement ORDER BY sensor_id, measurement_type, timestamp DESC", nativeQuery = true)
    List<Measurement> findLatestMeasurementsPerSensorAndType();

    long count();

    long countMeasurementByTimestampAfter(LocalDateTime timestamp);
}
