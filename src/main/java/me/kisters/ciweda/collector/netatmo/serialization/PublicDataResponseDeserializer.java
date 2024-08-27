package me.kisters.ciweda.collector.netatmo.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import me.kisters.ciweda.collector.netatmo.model.NetatmoStation;
import me.kisters.ciweda.collector.netatmo.model.Place;
import me.kisters.ciweda.collector.netatmo.model.PublicDataResponse;
import me.kisters.ciweda.db.entities.MeasurementType;
import me.kisters.ciweda.util.MacAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublicDataResponseDeserializer extends JsonDeserializer<PublicDataResponse> {

    private static final Logger log = LoggerFactory.getLogger(PublicDataResponseDeserializer.class);

    @Override
    public PublicDataResponse deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);

        // Beginne mit der Extraktion der Felder und der Handhabung von Abweichungen
        List<NetatmoStation> body = new ArrayList<>();
        if (root.has("body") && root.get("body").isArray()) {
            for (JsonNode stationNode : root.get("body")) {
                try {
                    NetatmoStation netAtmoStation = parseStationItem(stationNode, mapper);
                    body.add(netAtmoStation);
                } catch (JsonProcessingException e) {
                    log.error("Skipping Netatmo-station because of ", e);
                }
            }
        }

        String status = root.has("status") ? root.get("status").asText() : null;
        Optional<String> timeExec = root.has("time_exec") ? Optional.of(root.get("time_exec").asText()) : Optional.empty();
        String timeServer = root.has("time_server") ? root.get("time_server").asText() : null;

        return new PublicDataResponse(body, status, timeExec, timeServer);
    }

    private NetatmoStation parseStationItem(JsonNode stationNode, ObjectMapper mapper) throws JsonProcessingException {
        final MacAddress id = new MacAddress(stationNode.get("_id").asText());
        final Place stationPlace = mapper.treeToValue(stationNode.get("place"), Place.class);

        final NetatmoStation netatmoStation = new NetatmoStation(id, stationPlace);

        stationNode.get("measures").fields().forEachRemaining(jsonNodeEntry -> {
            // Measurements containing Temperature and Humidity, or Pressure
            log.debug("{} : {}", jsonNodeEntry.getKey(), jsonNodeEntry.getValue());
            JsonNode measurement = jsonNodeEntry.getValue();
            if (measurement.has("res")) {
                for(int i = 0; i < measurement.get("type").size(); ++i) {
                    final int currentTypeIndex = i;
                    final MeasurementType type = MeasurementType.valueOf(measurement.get("type").get(i).asText().toUpperCase());
                    measurement.get("res").fields().forEachRemaining(measurementSlot -> {
                        String time = measurementSlot.getKey();
                        LocalDateTime timeStamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(time)), ZoneOffset.UTC);
                        double value = measurementSlot.getValue().get(currentTypeIndex).asDouble();

                        netatmoStation.addMeasurement(timeStamp, type, value);
                    });
                }
            } else if(measurement.has("rain_timeutc")) {
                LocalDateTime timeStamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(measurement.get("rain_timeutc").asLong()), ZoneId.of("Europe/Berlin"));
                double value = measurement.get("rain_60min").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.RAIN_60MIN, value);
                value = measurement.get("rain_24h").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.RAIN_24H, value);
                value = measurement.get("rain_live").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.RAIN_LIVE, value);

            } else if (measurement.has("wind_timeutc")) {
                LocalDateTime timeStamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(measurement.get("wind_timeutc").asLong()), ZoneOffset.UTC);
                double value = measurement.get("wind_strength").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.WIND_STRENGTH, value);
                value = measurement.get("wind_angle").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.WIND_ANGLE, value);
                value = measurement.get("gust_strength").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.GUST_STRENGTH, value);
                value = measurement.get("gust_angle").asDouble();
                netatmoStation.addMeasurement(timeStamp, MeasurementType.GUST_ANGLE, value);

            } else {
                throw new IllegalArgumentException("Unknown measurement: " + measurement.toPrettyString());
            }
        });
        return netatmoStation;
    }
}
