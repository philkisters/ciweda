package me.kisters.ciweda.db.repositories;

import me.kisters.ciweda.db.entities.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findBySourceAndOriginalId(String source, String originalId);
    List<Sensor> findBySource(String source);

    long count();
}
