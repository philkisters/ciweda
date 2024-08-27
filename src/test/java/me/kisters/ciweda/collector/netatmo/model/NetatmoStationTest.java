package me.kisters.ciweda.collector.netatmo.model;

import me.kisters.ciweda.db.entities.MeasurementType;
import me.kisters.ciweda.util.MacAddress;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NetatmoStationTest {

    @Test
    void hasMeasurementOfType() {
        List<Double> position = new ArrayList<>();
        position.add(53.11);
        position.add(9.91);
        NetatmoStation netatmoStation = new NetatmoStation(new MacAddress("00:00:00:00:00:00"), new Place("UTC", "DE", 10, position, "Hamburg", "Vogt-Kölln-Straße" ));
        netatmoStation.addMeasurement(LocalDateTime.now(), MeasurementType.WIND_ANGLE, 12.0);

        assertTrue(netatmoStation.hasMeasurementOfType(MeasurementType.WIND_ANGLE));
        assertFalse(netatmoStation.hasMeasurementOfType(MeasurementType.WIND_STRENGTH));
    }
}