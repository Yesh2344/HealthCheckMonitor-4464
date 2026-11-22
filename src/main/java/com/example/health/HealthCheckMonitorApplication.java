package com.example.health;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the HealthCheckMonitor Spring Boot application.
 */
@SpringBootApplication
public class HealthCheckMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthCheckMonitorApplication.class, args);
// tiny readability tweak
    }
}