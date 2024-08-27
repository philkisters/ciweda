package me.kisters.ciweda.collector.netatmo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetatmoConfig {
    @Value("${ciweda.sources.netatmo.publicurl}")
    private String publicUrl;

    private final String bearer;

    private final double latStart;
    private final double lonStart;

    private final double latEnd;
    private final double lonEnd;

    public NetatmoConfig(Dotenv dotenv) {
        bearer = dotenv.get("NETATMO_BEARER");
        latStart = Double.parseDouble(dotenv.get("AREA_MAX_LAT"));
        lonStart = Double.parseDouble(dotenv.get("AREA_MAX_LON"));
        latEnd = Double.parseDouble(dotenv.get("AREA_MIN_LAT"));
        lonEnd = Double.parseDouble(dotenv.get("AREA_MIN_LON"));
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public String getBearer() {
        return bearer;
    }

    public double getLatStart() {
        return latStart;
    }

    public double getLonStart() {
        return lonStart;
    }

    public double getLatEnd() {
        return latEnd;
    }

    public double getLonEnd() {
        return lonEnd;
    }
}
