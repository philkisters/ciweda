package me.kisters.ciweda.collector.netatmo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetatmoConfig {
    @Value("${ciweda.sources.netatmo.publicurl}")
    private String publicUrl;

    private final String bearer;

    public NetatmoConfig(Dotenv dotenv) {
        bearer = dotenv.get("NETATMO_BEARER");
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public String getBearer() {
        return bearer;
    }

}
