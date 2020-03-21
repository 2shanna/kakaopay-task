package com.kakaopay.greentour.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthCheckControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("health check")
    void healthCheck() throws Exception  {
        webTestClient.get()
                .uri("/health_check")
                .exchange()
                .expectStatus().isOk();
    }
}