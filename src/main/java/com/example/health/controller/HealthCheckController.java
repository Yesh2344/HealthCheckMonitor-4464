package com.example.health.controller;

import com.example.health.service.HealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing the aggregated health status.
 */
@RestController
@Slf4j
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    /**
     * GET {@code /health}
     *
     * @return JSON map where each key is an endpoint URL and the value is {@code "UP"} or {@code "DOWN"}.
     */
    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        try {
            var statusMap = healthCheckService.getCurrentStatus();
            var result = statusMap.entrySet()
                    .stream()
                    .collect(java.util.stream.Collectors.toMap(
                            java.util.Map.Entry::getKey,
                            e -> e.getValue() ? "UP" : "DOWN"
                    ));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to retrieve health status", e);
            return ResponseEntity.status(500).body("Unable to fetch health status");
        }
    }
}