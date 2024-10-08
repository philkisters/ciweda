package me.kisters.ciweda.db;

import me.kisters.ciweda.db.entities.CollectorStatistics;
import me.kisters.ciweda.db.entities.Measurement;
import me.kisters.ciweda.db.entities.MeasurementType;
import me.kisters.ciweda.db.entities.Sensor;
import me.kisters.ciweda.db.repositories.CollectorStatisticsRepository;
import me.kisters.ciweda.db.repositories.MeasurementRepository;
import me.kisters.ciweda.db.repositories.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataService {

    private static final Logger log = LoggerFactory.getLogger(DataService.class);
    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;
    private final CollectorStatisticsRepository collectorStatisticsRepository;
    private final Map<Long, Map<MeasurementType,Measurement>> latestMeasurementsMap;

    @Autowired
    public DataService(MeasurementRepository measurementRepository, SensorRepository sensorRepository, CollectorStatisticsRepository collectorStatisticsRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
        this.collectorStatisticsRepository = collectorStatisticsRepository;

        List<Measurement> latestMeasurements = measurementRepository.findLatestMeasurementsPerSensorAndType();
        latestMeasurementsMap = new HashMap<>();
        latestMeasurements.forEach(measurement -> {
            if (latestMeasurementsMap.containsKey(measurement.getSensor().getId())) {
                latestMeasurementsMap.get(measurement.getSensor().getId()).put(measurement.getMeasurementType(), measurement);
            } else {
                Map<MeasurementType, Measurement> measurementTypesMeasurementsMap = new HashMap<>();
                measurementTypesMeasurementsMap.put(measurement.getMeasurementType(), measurement);
                latestMeasurementsMap.put(measurement.getSensor().getId(), measurementTypesMeasurementsMap);
            }
        });
    }

    public Sensor saveSensor(final Sensor sensor, final CollectorStatistics stats) {
        Optional<Sensor> existingSensor = sensorRepository.findBySourceAndOriginalId(sensor.getSource(), sensor.getOriginalId());

        if (existingSensor.isPresent()) {
            // TODO prüfen ob der Sensor sich verändert hat (neue/weniger Daten, neuer Ort)
            log.debug("New data for existing sensor: {}",  existingSensor.get().getId());
            return existingSensor.get();
        }
        Sensor savedSensor = sensorRepository.save(sensor);
        log.debug("New sensor saved in database: {}", savedSensor.getId());
        stats.addNewSensorsCount(1);
        return savedSensor;
    }

    public CollectorStatistics saveCollectorStatistics(final CollectorStatistics collectorStatistics) {
        return collectorStatisticsRepository.save(collectorStatistics);
    }

    public Measurement saveMeasurement(Measurement measurement) {
        List<Measurement> existingMeasurements = measurementRepository.findMeasurementsBySensorAndMeasurementTypeAndTimestamp(measurement.getSensor(), measurement.getMeasurementType(), measurement.getTimestamp());
        if (!existingMeasurements.isEmpty()) {
            log.debug("Measurement already exists with the same timestamp. Measurement {} collected values old {} new {}", existingMeasurements.getFirst().getId(), existingMeasurements.getFirst().getValue(), measurement.getValue());
            return existingMeasurements.getFirst();
        }
        Measurement savedMeasurement = measurementRepository.save(measurement);
        log.debug("Measurement saved in database: {}", savedMeasurement.getId());
        return measurement;
    }

    public long getTotalMeasurementCount() {
        return measurementRepository.count();
    }

    public long getMeasurementCountLastDay() {
        return measurementRepository.countMeasurementByTimestampAfter(LocalDateTime.now().minusDays(1));
    }

    public long getMeasurementCountLastWeek() {
        return measurementRepository.countMeasurementByTimestampAfter(LocalDateTime.now().minusDays(7));
    }

    public long getMeasurementCountLastMonth() {
        return measurementRepository.countMeasurementByTimestampAfter(LocalDateTime.now().minusMonths(1));
    }

    public long getSensorCount() {
        return sensorRepository.count();
    }

    public int saveMeasurements(Set<Measurement> measurements) {

        Set<Measurement> measurementsToSave = new HashSet<>();

        for (Measurement newMeasurement : measurements) {
            if (!latestMeasurementsMap.containsKey(newMeasurement.getSensor().getId())) {
                // Sensor doesn't exist add to current latestMeasurementsMap
                Map<MeasurementType, Measurement> measurementTypesMeasurementsMap = new HashMap<>();
                measurementTypesMeasurementsMap.put(newMeasurement.getMeasurementType(), newMeasurement);
                latestMeasurementsMap.put(newMeasurement.getSensor().getId(), measurementTypesMeasurementsMap);
                // Save Measurement in DB
                measurementsToSave.add(newMeasurement);
                continue;
            }

            Measurement latestMeasurement = latestMeasurementsMap.get(newMeasurement.getSensorId()).get(newMeasurement.getMeasurementType());
            if (latestMeasurement == null || newMeasurement.getTimestamp().isAfter(latestMeasurement.getTimestamp())) {
                // Save when new timestamp or type does not exist.
                latestMeasurementsMap.get(newMeasurement.getSensorId()).put(newMeasurement.getMeasurementType(), newMeasurement);
                measurementsToSave.add(newMeasurement);
            }
        }

        measurementRepository.saveAll(measurementsToSave);
        return measurementsToSave.size();
    }

    /**
     * Retrieves all existing Sensors from a given source and returns them as a map where the original ID (id retrieved by the source) maps at the sensors.
     *
     * @param source A data source that has provided data beforehand.
     * @return Map with all existing sensors from the given data source. Original ID as the key for easier retrieval.
     */
    public Map<String, Sensor> getSensorsFromSource(String source) {
        List<Sensor> sensors = sensorRepository.findBySource(source);
        Map<String, Sensor> sensorMap = new HashMap<>();
        sensors.forEach(sensor -> sensorMap.put(sensor.getOriginalId(), sensor));

        return sensorMap;
    }
}
