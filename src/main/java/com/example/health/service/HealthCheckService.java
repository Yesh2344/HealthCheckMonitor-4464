package com.example.health.service;

import com.example.health.config.AppConfig;
import com.example.health.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for periodically checking the health of configured endpoints.
 */
@Service
@Slf4j
public class HealthCheckService {

    private final AppConfig appConfig;

    /**
     * Map containing the latest health status for each endpoint.
     * The key is the endpoint URL, the value is {@code true} for UP, {@code false} for DOWN.
     */
    private final Map<String, Boolean> healthStatus = Collections.synchronizedMap(new HashMap<>());

    public HealthCheckService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Initializes the health status map after bean construction.
     */
    @PostConstruct
    public void init() {
        if (appConfig.getEndpoints() == null || appConfig.getEndpoints().isBlank()) {
            log.warn("No health endpoints configured. Service will be idle.");
            return;
        }
        for (String endpoint : parseEndpoints()) {
            healthStatus.put(endpoint, false);
        }
        // Perform an immediate check on startup
        performHealthCheck();
    }

    /**
     * Scheduled task that runs at a fixed rate defined by {@code app.health.check-interval-ms}.
     */
    @Scheduled(fixedRateString = "#{@appConfig.checkIntervalMs}")
    public void performHealthCheck() {
        if (healthStatus.isEmpty()) {
            log.debug("Health check skipped: no endpoints configured.");
            return;
        }
        log.info("Starting health check for {} endpoints.", healthStatus.size());
        for (String endpoint : healthStatus.keySet()) {
            try {
                boolean healthy = HttpClientUtil.isEndpointHealthy(endpoint);
                healthStatus.put(endpoint, healthy);
                log.info("Endpoint {} is {}", endpoint, healthy ? "UP" : "DOWN");
            } catch (IOException | InterruptedException e) {
                healthStatus.put(endpoint, false);
                log.error("Error checking endpoint {}: {}", endpoint, e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Returns an immutable snapshot of the current health status.
     *
     * @return map of endpoint → status (true = UP, false = DOWN)
     */
    public Map<String, Boolean> getCurrentStatus() {
        return Collections.unmodifiableMap(new HashMap<>(healthStatus));
    }

    /**
     * Parses the comma‑separated endpoint list from configuration.
     *
     * @return array of trimmed endpoint URLs.
     */
    private String[] parseEndpoints() {
        return appConfig.getEndpoints()
                .split("\\s*,\\s*");
    }
}