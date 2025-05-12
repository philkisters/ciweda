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
    RAIN_LIVE,
    PRESSURE_24H,
    TEMPERATURE_24H,
    HUMIDITY_24H,
    WIND_STRENGTH_24H,
    GUST_STRENGTH_24H_MAX,
    CLOUD_COVERAGE_24H,
    PRECIPITATION_TYPE,
    SUN_24H,
    SNOW_24H;


    public static String getUnitForType(MeasurementType type) {
        return switch (type) {
            case PRESSURE, PRESSURE_24H -> "mBar";
            case TEMPERATURE, TEMPERATURE_24H -> "Celsius";
            case HUMIDITY, HUMIDITY_24H -> "percentage";
            case WIND_STRENGTH, GUST_STRENGTH, WIND_STRENGTH_24H, GUST_STRENGTH_24H_MAX -> "km/h";
            case WIND_ANGLE, GUST_ANGLE -> "degrees";
            case RAIN_60MIN, RAIN_24H, RAIN_LIVE, SNOW_24H -> "mm";
            case CLOUD_COVERAGE_24H -> "eights";
            case PRECIPITATION_TYPE -> "code";
            case SUN_24H -> "hours";
            default -> "Unknown";
        };
    }
}
