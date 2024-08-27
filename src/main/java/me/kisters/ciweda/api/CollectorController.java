package me.kisters.ciweda.api;

import me.kisters.ciweda.collector.CollectorService;
import me.kisters.ciweda.util.CollectorStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collectors")
public class CollectorController{
    private final CollectorService collectorService;

    @Autowired
    public CollectorController(final CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @GetMapping("/status")
    public List<CollectorStatus> getAllCollectorStatuses() {
        return collectorService.getStatusList();
    }
}
