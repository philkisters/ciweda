package me.kisters.ciweda.collector.netatmo.model;

import java.util.List;
import java.util.Optional;

public class PublicDataResponse {
    private final List<NetatmoStation> netatmoStations;
    private final String status;
    private final Optional<String> timeExec;
    private final String timeServer;

    public PublicDataResponse(final List<NetatmoStation> netatmoStations, final String status, final Optional<String> timeExec, final String timeServer) {
        this.netatmoStations = netatmoStations;
        this.status = status;
        this.timeExec = timeExec;
        this.timeServer = timeServer;
    }

    public String getStatus() {
        return status;
    }

    public List<NetatmoStation> getNetatmoStations() {
        return netatmoStations;
    }

    @Override
    public String toString() {
        return "PublicDataResponse{" +
                "netatmoStations=" + netatmoStations +
                ", status='" + status + '\'' +
                ", timeExec=" + timeExec +
                ", timeServer='" + timeServer + '\'' +
                '}';
    }
}
