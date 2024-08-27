package me.kisters.ciweda.collector.netatmo.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.kisters.ciweda.collector.netatmo.model.NetatmoStation;
import me.kisters.ciweda.collector.netatmo.model.Place;
import me.kisters.ciweda.collector.netatmo.model.PublicDataResponse;
import me.kisters.ciweda.db.entities.MeasurementType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PublicDataResponseDeserializerTest {


    @Test
    void deserialize() throws JsonProcessingException {
        String response = """
                {
                    "status": "ok",
                    "time_server": 1724231976,
                    "body": [
                        {
                            "_id": "70:ee:50:96:d3:7e",
                            "place": {
                                "location": [
                                    9.945716,
                                    53.586399
                                ],
                                "timezone": "Europe/Berlin",
                                "country": "DE",
                                "altitude": 18,
                                "city": "Hamburg",
                                "street": "Leopardenstraße"
                            },
                            "mark": 10,
                            "measures": {
                                "02:00:00:96:bc:70": {
                                    "res": {
                                        "1724231441": [
                                            18.1,
                                            74
                                        ]
                                    },
                                    "type": [
                                        "temperature",
                                        "humidity"
                                    ]
                                },
                                "70:ee:50:96:d3:7e": {
                                    "res": {
                                        "1724231451": [
                                            1011.6
                                        ]
                                    },
                                    "type": [
                                        "pressure"
                                    ]
                                }
                            },
                            "modules": [
                                "02:00:00:96:bc:70"
                            ],
                            "module_types": {
                                "02:00:00:96:bc:70": "NAModule1"
                            }
                        },
                        {
                            "_id": "70:ee:50:80:23:88",
                            "place": {
                                "location": [
                                    9.950687,
                                    53.593807
                                ],
                                "timezone": "Europe/Berlin",
                                "country": "DE",
                                "altitude": 12,
                                "city": "Hamburg",
                                "street": "Max-Tau-Straße"
                            },
                            "mark": 10,
                            "measures": {
                                "02:00:00:7f:cd:2a": {
                                    "res": {
                                        "1724231405": [
                                            16.4,
                                            78
                                        ]
                                    },
                                    "type": [
                                        "temperature",
                                        "humidity"
                                    ]
                                },
                                "70:ee:50:80:23:88": {
                                    "res": {
                                        "1724231426": [
                                            1011.9
                                        ]
                                    },
                                    "type": [
                                        "pressure"
                                    ]
                                },
                                "05:00:00:09:08:56": {
                                    "rain_60min": 2.6260000000000003,
                                    "rain_24h": 16.463,
                                    "rain_live": 1.3130000000000002,
                                    "rain_timeutc": 1724231418
                                },
                                "06:00:00:05:c1:d0": {
                                    "wind_strength": 3,
                                    "wind_angle": 359,
                                    "gust_strength": 9,
                                    "gust_angle": 359,
                                    "wind_timeutc": 1724231425
                                }
                            },
                            "modules": [
                                "02:00:00:7f:cd:2a",
                                "05:00:00:09:08:56",
                                "06:00:00:05:c1:d0"
                            ],
                            "module_types": {
                                "02:00:00:7f:cd:2a": "NAModule1",
                                "05:00:00:09:08:56": "NAModule3",
                                "06:00:00:05:c1:d0": "NAModule2"
                            }
                        }
                    ]
                }
        """;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(PublicDataResponse.class, new PublicDataResponseDeserializer()));

        PublicDataResponse result = mapper.readValue(response, PublicDataResponse.class);

        assertNotNull(response);
        assertEquals("ok", result.getStatus());
        assertEquals(2, result.getNetatmoStations().size());

        NetatmoStation stationItem = result.getNetatmoStations().get(0);
        assertEquals("70:ee:50:96:d3:7e", stationItem.getId().getAddress());

        assertFalse(stationItem.hasMeasurementOfType(MeasurementType.WIND_STRENGTH));
        assertEquals(Optional.empty(), stationItem.getMeasurementOfType(MeasurementType.WIND_STRENGTH));

        List<Double> location = new ArrayList<>();
        location.add(9.945716);
        location.add(53.586399);
        assertEquals(new Place("Europe/Berlin", "DE", 18, location , "Hamburg", "Leopardenstraße" ), stationItem.getPlace());


        stationItem = result.getNetatmoStations().get(1);
        assertEquals("70:ee:50:80:23:88", stationItem.getId().getAddress());

        assertTrue(stationItem.hasMeasurementOfType(MeasurementType.WIND_STRENGTH));
        assertEquals(3.0, stationItem.getMeasurementOfType(MeasurementType.WIND_STRENGTH).get());

        assertEquals(10, stationItem.getMeasurementCount());
    }
}
