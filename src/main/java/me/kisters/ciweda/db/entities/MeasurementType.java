package me.kisters.ciweda.db.entities;

public enum MeasurementType {
    PRESSURE,
    TEMPERATURE,
    HUMIDITY,
    WIND_STRENGTH,
    WIND_ANGLE,
    GUST_STRENGTH,
    GUST_ANGLE,
    RAIN_60MIN,
    RAIN_24H,
    RAIN_LIVE;

    public static String getUnitForType(MeasurementType type) {
        return switch (type) {
            case PRESSURE -> "mBar";
            case TEMPERATURE -> "Celsius";
            case HUMIDITY -> "percentage";
            case WIND_STRENGTH, GUST_STRENGTH -> "km/h";
            case WIND_ANGLE, GUST_ANGLE -> "degrees";
            case RAIN_60MIN, RAIN_24H, RAIN_LIVE -> "mm";
            default -> "Unknown";
        };
    }
}
