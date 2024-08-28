package me.kisters.ciweda.collector.netatmo.model;

import me.kisters.ciweda.db.entities.Measurement;
import me.kisters.ciweda.db.entities.MeasurementType;
import me.kisters.ciweda.db.entities.Sensor;
import me.kisters.ciweda.util.MacAddress;

import java.time.LocalDateTime;
import java.util.*;

public class NetatmoStation {
    private final MacAddress id;
    private final Place place;
    private final List<MeasurementType> measurementTypes;
    private final List<Measurement> lastMeasurements;

    public NetatmoStation(MacAddress id, Place stationPlace) {
        this.id = id;
        this.place = stationPlace;
        this.measurementTypes = new ArrayList<>();
        this.lastMeasurements = new ArrayList<>();
    }

    public void addMeasurement(LocalDateTime timestamp, MeasurementType measurementType, Double value) {

        if (measurementTypes.contains(measurementType)) {
            throw new IllegalStateException("Measurement type " + measurementType + " already exists");
        }

        measurementTypes.add(measurementType);
        lastMeasurements.add(new Measurement(timestamp, this.place.toGeoPoint(), measurementType, value, MeasurementType.getUnitForType(measurementType)));
    }

    public boolean hasMeasurementOfType(MeasurementType measurementType) {
        return lastMeasurements.stream().anyMatch(measurement -> measurement.getMeasurementType() == measurementType);
    }

    public MacAddress getId() {
        return id;
    }

    public Place getPlace() {
        return place;
    }

    public List<MeasurementType> getMeasurementTypes() {
        return measurementTypes;
    }

    public Sensor toSensor() {
        return new Sensor("Netatmo", id.toString(), place.toGeoPoint(), getMeasurementTypes(), "");
    }

    /**
     * Returns the last measured value of the given type.
     * If that station doesn't have that measurement type it returns an empty optional.
     * @param measurementType measurement type of the searched value.
     * @return Optional of that value or empty, when no measurement of the given type exists.
     */
    public Optional<Double> getMeasurementOfType(MeasurementType measurementType) {
        for (Measurement measurement : lastMeasurements) {
            if (measurement.getMeasurementType() == measurementType) {
                return Optional.of(measurement.getValue());
            }
        }
        return Optional.empty();
    }

    public int getMeasurementCount() {
        return lastMeasurements.size();
    }

    public final List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(lastMeasurements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetatmoStation that = (NetatmoStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
