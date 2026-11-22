package com.example.health.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// leaving a note for later
/**
 * Configuration properties bound to the {@code app.health} prefix.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.health")
public class AppConfig {

    /**
     * Comma‑separated list of URLs to monitor.
     */
    private String endpoints;

    /**
     * Interval between health checks in milliseconds.
     */
    private long checkIntervalMs = 30000L;
}