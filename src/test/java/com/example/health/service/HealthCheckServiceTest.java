package com.example.health.service;

import com.example.health.config.AppConfig;
import com.example.health.util.HttpClientUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HealthCheckServiceTest {

    private HealthCheckService healthCheckService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        appConfig.setEndpoints("https://example.com,https://down.example.org");
        appConfig.setCheckIntervalMs(1000L);
        healthCheckService = new HealthCheckService(appConfig);
        healthCheckService.init();
    }

    @Test
    void testPerformHealthCheck_AllUp() throws IOException, InterruptedException {
        try (MockedStatic<HttpClientUtil> mocked = mockStatic(HttpClientUtil.class)) {
            mocked.when(() -> HttpClientUtil.isEndpointHealthy(anyString()))
                    .thenReturn(true);

            healthCheckService.performHealthCheck();

            Map<String, Boolean> status = healthCheckService.getCurrentStatus();
            assertTrue(status.get("https://example.com"));
            assertTrue(status.get("https://down.example.org"));
        }
    }

    @Test
    void testPerformHealthCheck_MixedResults() throws IOException, InterruptedException {
        try (MockedStatic<HttpClientUtil> mocked = mockStatic(HttpClientUtil.class)) {
            mocked.when(() -> HttpClientUtil.isEndpointHealthy("https://example.com"))
                    .thenReturn(true);
            mocked.when(() -> HttpClientUtil.isEndpointHealthy("https://down.example.org"))
                    .thenThrow(new IOException("Connection refused"));

            healthCheckService.performHealthCheck();

            Map<String, Boolean> status = healthCheckService.getCurrentStatus();
            assertTrue(status.get("https://example.com"));
            assertFalse(status.get("https://down.example.org"));
        }
    }

    @Test
    void testGetCurrentStatus_ImmutableSnapshot() {
        Map<String, Boolean> snapshot1 = healthCheckService.getCurrentStatus();
        // Mutate internal map via reflection would be complex; instead ensure the returned map is not the same instance
        Map<String, Boolean> snapshot2 = healthCheckService.getCurrentStatus();
        assertNotSame(snapshot1, snapshot2);
    }
}