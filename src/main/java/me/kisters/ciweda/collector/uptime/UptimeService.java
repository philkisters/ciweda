package me.kisters.ciweda.collector.uptime;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UptimeService {

    private static final Logger log = LoggerFactory.getLogger(UptimeService.class);
    private final WebClient webClient;
    private final String uptimeUrl;
    private final String mattermostUrl;

    @Autowired
    public UptimeService(WebClient webClient, Dotenv dotenv) {
        this.webClient = webClient;
        this.uptimeUrl = dotenv.get("UPTIME_URL");
        this.mattermostUrl = dotenv.get("MATTERMOST_URL");
    }

    public void sendUptimePing() {
        log.info("Sending uptime ping");
        webClient.get()
                .uri(uptimeUrl)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public void sendMattermostError(String collectorName, String areaName, String errorMsg) {
        MattermostMessage msg = new MattermostMessage(String.format("@kisters\nCollector **%s** received the following error '%s' for the area *%s*", collectorName, errorMsg, areaName));
        log.info("Sending error message: {}", msg);
        webClient.post()
                .uri(mattermostUrl)
                .body(Mono.just(msg), MattermostMessage.class)  // MattermostMessage als Body übergeben
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
