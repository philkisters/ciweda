package me.kisters.ciweda.api.model;

import java.util.Map;

public record MattermostResponse(String response_type, String username, String icon_url, String text, Map<String, Long> props) {
}
