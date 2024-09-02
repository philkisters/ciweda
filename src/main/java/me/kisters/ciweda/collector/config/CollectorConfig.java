package me.kisters.ciweda.collector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "ciweda.collector")
public class CollectorConfig {

    private Map<String, Area> areas;

    public Map<String, Area> getAreas() {
        return areas;
    }

    public void setAreas(Map<String, Area> areas) {
        this.areas = areas;
    }
}