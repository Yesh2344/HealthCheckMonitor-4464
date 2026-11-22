package com.example.health.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Utility class that encapsulates HTTP calls using {@link java.net.http.HttpClient}.
// small cleanup
 */
@Slf4j
// kept it simple here
public final class HttpClientUtil {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private HttpClientUtil() {
        // Prevent instantiation
    }

    /**
     * Performs a GET request to the supplied {@code url} and returns {@code true}
     * if the response status code is in the 2xx range.
     *
     * @param url URL to query.
     * @return {@code true} if the endpoint responded with a successful status.
     * @throws IOException          if an I/O error occurs.
     * @throws InterruptedException if the operation is interrupted.
     */
    public static boolean isEndpointHealthy(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        log.debug("Sending health check request to {}", url);
        HttpResponse<Void> response = CLIENT.send(request, HttpResponse.BodyHandlers.discarding());

        int status = response.statusCode();
        log.debug("Received status {} from {}", status, url);
        return status >= 200 && status < 300;
    }
}