package me.kisters.ciweda.collector;

import me.kisters.ciweda.util.CollectorStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CollectorService {
    private final Map<String, Collector> collectors;
    public CollectorService() {
        this.collectors = new HashMap<>();
    }

    public void addCollector(Collector collector) {
        this.collectors.put(collector.getName(), collector);
    }

    public List<CollectorStatus> getStatusList() {
        List<CollectorStatus> statusList = new ArrayList<>();
        for (Collector collector : this.collectors.values()) {
            statusList.add(collector.getStatus());
        }
        return statusList;
    }
}
