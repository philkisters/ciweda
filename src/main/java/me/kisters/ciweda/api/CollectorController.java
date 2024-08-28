package me.kisters.ciweda.api;

import me.kisters.ciweda.api.model.MattermostResponse;
import me.kisters.ciweda.collector.CollectorService;
import me.kisters.ciweda.util.CollectorStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/collectors")
public class CollectorController{
    private static final Logger log = LoggerFactory.getLogger(CollectorController.class);
    private final CollectorService collectorService;

    @Autowired
    public CollectorController(final CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @GetMapping("/status")
    public List<CollectorStatus> getAllCollectorStatuses() {
        return collectorService.getStatusList();
    }

    @PostMapping("/mattermost")
    public ResponseEntity<MattermostResponse> postMattermostCollectorStatuses(@RequestParam Map<String, String> params) {
        String token = params.get("token");
        String text = params.get("text");

        log.info("Received POST Mattermost webhook with Token: {} and Text: {}", token, text);
        return ResponseEntity.ok(new MattermostResponse("comment", "CollectorBot", "", "Super awesome test", new HashMap<>()));
    }
}
