package me.kisters.ciweda.collector.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.kisters.ciweda.collector.netatmo.model.PublicDataResponse;
import me.kisters.ciweda.collector.netatmo.serialization.PublicDataResponseDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // Netatmo Deserializer
        module.addDeserializer(PublicDataResponse.class, new PublicDataResponseDeserializer());


        mapper.registerModule(module);
        return mapper;
    }

    @Bean
    public WebClient webClient(ObjectMapper customObjectMapper) {
        return WebClient.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs()
                                .jackson2JsonDecoder(new Jackson2JsonDecoder(customObjectMapper));
                    configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024);
                }).build();
    }
}