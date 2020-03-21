package com.kakaopay.greentour.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.time.Duration;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileUploadControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    @Test
    @DisplayName("upload file success")
    void uploadFile() throws Exception {
        String fileDirectory = "./src/test/resources/";
        String fileName = "사전과제2.csv";
        String filePath = String.format("%s%s", fileDirectory, fileName);
        File csvFile = new File(filePath);

        webTestClient.post()
                .uri("/upload")
                .contentType(MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file", new FileSystemResource(csvFile)))
                .exchange()
                .expectStatus().isCreated();
    }
}
