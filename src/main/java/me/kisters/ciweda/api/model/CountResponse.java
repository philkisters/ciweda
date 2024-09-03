package me.kisters.ciweda.api.model;

public record CountResponse(long totalMeasurements, long dailyMeasurements, long weeklyMeasurements, long monthlyMeasurements) {
}
