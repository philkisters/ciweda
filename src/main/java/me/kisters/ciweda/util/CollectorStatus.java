package me.kisters.ciweda.util;

import me.kisters.ciweda.db.entities.CollectorStatistics;

public class CollectorStatus {
    private Status status;
    private String errorMessage;
    private final String name;
    private long timeStamp;
    private CollectorStatistics lastStatistics;

    public CollectorStatus(final String name) {
        this.errorMessage = "";
        this.name = name;
        this.status = Status.OK;
        this.timeStamp = System.currentTimeMillis();
        this.lastStatistics = new CollectorStatistics();
    }

    public void resetError() {
        this.errorMessage = "";
        this.status = Status.OK;
    }

    public void setError(final Throwable error) {
        this.errorMessage = error.getMessage();
        this.status = Status.ERROR;
    }

    public void updateLastCollection() {
        this.timeStamp = System.currentTimeMillis();
        this.status = Status.IN_PROGRESS;
    }

    public void setLastStatistics(final CollectorStatistics lastStatistics) {
        this.lastStatistics = lastStatistics;
        this.status = Status.OK;
    }

    public Status getStatus() {
        return status;
    }

    public String getError() {
        return errorMessage;
    }

    public String getName() {
        return name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public CollectorStatistics getLastStatistics() {
        return lastStatistics;
    }

    @Override
    public String toString() {
        return "CollectorStatus{" +
                "status=" + status +
                ", errorMessage='" + errorMessage + '\'' +
                ", name='" + name + '\'' +
                ", timeStamp=" + timeStamp +
                ", lastStatistics=" + lastStatistics +
                '}';
    }
}
