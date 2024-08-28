package me.kisters.ciweda.collector.netatmo;

import me.kisters.ciweda.collector.Collector;
import me.kisters.ciweda.collector.CollectorService;
import me.kisters.ciweda.collector.netatmo.model.NetatmoStation;
import me.kisters.ciweda.collector.netatmo.model.PublicDataResponse;
import me.kisters.ciweda.db.DataService;
import me.kisters.ciweda.db.entities.CollectorStatistics;
import me.kisters.ciweda.db.entities.Measurement;
import me.kisters.ciweda.db.entities.Sensor;
import me.kisters.ciweda.util.CollectorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NetatmoCollectorService implements Collector {
    private static final String NAME = "Netatmo";
    private static final Logger log = LoggerFactory.getLogger(NetatmoCollectorService.class);
    private final DataService dataService;
    private final WebClient webClient;
    private final NetatmoConfig config;
    private final CollectorStatus status;

    @Autowired
    public NetatmoCollectorService(DataService dataService, WebClient webClient, NetatmoConfig config, CollectorService collectorService) {
        this.dataService = dataService;
        this.webClient = webClient;
        this.config = config;
        this.status = new CollectorStatus(NAME);

        collectorService.addCollector(this);
    }

    @Scheduled(fixedRate = 250000, initialDelay = 10000)
    public void collectNetatmo() {
        final int rows = 2;
        final int cols = 2;

        final double latDecrease = (config.getLatStart() - config.getLatEnd()) / rows;
        final double lonDecrease = (config.getLonStart() - config.getLonEnd()) / cols;

        List<String> urls = new ArrayList<>();

        CollectorStatistics stats = new CollectorStatistics(NAME);
        status.updateLastCollection();

        log.info("Started collecting Netatmo data with a {}x{} grid. With a latitude grid size {} and longitude grid size {}", rows, cols, latDecrease, lonDecrease);
        for (int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j) {
                StringBuilder sb = new StringBuilder(config.getPublicUrl());
                sb.append("?lat_ne=").append(config.getLatStart() - i * latDecrease);
                sb.append("&lon_ne=").append(config.getLonStart() - j * lonDecrease);
                sb.append("&lat_sw=").append(config.getLatStart() - (i+1) * latDecrease);
                sb.append("&lon_sw=").append(config.getLonStart() - (j+1) * lonDecrease);
                sb.append("&filter=false");
                urls.add(sb.toString());
                log.debug("Created url: {}", sb);
            }
        }
        Collections.shuffle(urls);
        Map<String, Sensor> existingSensors = dataService.getSensorsFromSource(NAME);
        log.debug("Loaded {} sensors from source 'Netatmo'.", existingSensors.size());

        Set<Measurement> measurementsToSave = new HashSet<>();
        Set<NetatmoStation> stations = new HashSet<>();

        Flux.fromIterable(urls)
                .delayElements(Duration.ofMillis(254))
                .flatMap(this::callNetatmoApi)
                .collectList()
                .doOnSuccess(responses -> processResponses(responses, stations, existingSensors, stats, measurementsToSave))
                .doOnError(throwable -> {
                    log.error("Error when calling Netatmo api: ", throwable);
                    status.setError(throwable);
                })
                .subscribe();
    }

    private void processResponses(List<PublicDataResponse> responses, Set<NetatmoStation> stations, Map<String, Sensor> existingSensors, CollectorStatistics stats, Set<Measurement> measurementsToSave) {
        log.info("Collected all {} Netatmo responses", responses.size());
        responses.forEach(response -> stations.addAll(response.getNetatmoStations()));
        stations.forEach(station -> {
            final Sensor savedSensor = existingSensors.containsKey(station.getId().toString()) ? existingSensors.get(station.getId().toString()) : dataService.saveSensor(station.toSensor(), stats);
            station.getMeasurements().forEach(measurement -> {
                measurement.setSensor(savedSensor);
                measurementsToSave.add(measurement);
            });
        });

        stats.addReceivedSensorsCount(stations.size());
        int savedMeasurements = dataService.saveMeasurements(measurementsToSave);
        stats.addReceivedMeasurementsCount(measurementsToSave.size());
        stats.addNewMeasurementsCount(savedMeasurements);
        status.setLastStatistics(dataService.saveCollectorStatistics(stats));
        log.info(stats.toString());
    }

    private Mono<PublicDataResponse> callNetatmoApi(String apiUrl) {
        return webClient.get()
                .uri(apiUrl)
                .headers(headers -> headers.setBearerAuth(config.getBearer()))
                .retrieve()
                .bodyToMono(PublicDataResponse.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public CollectorStatus getStatus() {
        log.debug("Service requests my status {}", status);
        return status;
    }
}
