package com.kakaopay.greentour.controller;

import com.kakaopay.greentour.dto.EcoInformation;
import com.kakaopay.greentour.service.ApplicationUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreenTourControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationUserDetailsService applicationUserDetailsService;

    private String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + applicationUserDetailsService.createToken("ryan");
    }

    @Test
    @DisplayName("find eco information by region code")
    void findEcoInfoByRegionCd() throws Exception  {
        String regionCd = "reg00001";

        webTestClient.get()
                .uri("/greentour/ecoinfo/region/" + regionCd)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("register eco information")
    void registerEcoInfo() throws Exception  {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램", "자연휴양림, 국립공원",
                "강원도 평창군", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        webTestClient.post()
                .uri("/greentour/ecoinfo")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .body(Mono.just(ecoInfo), EcoInformation.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("update eco information")
    void updateEcoInfo() throws Exception  {
        EcoInformation ecoInfo = new EcoInformation(200, "테스트 프로그램", "자연휴양림, 국립공원",
                "강원도 평창군", "테스트 프로그램입니다", "테스트 프로그램입니다. 디테일입니다.");

        webTestClient.put()
                .uri("/greentour/ecoinfo")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .body(Mono.just(ecoInfo), EcoInformation.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("find region and program(name & theme) by region name")
    void findByRegionName() throws Exception  {
        String regionName = "평창군";
        webTestClient.get()
                .uri("/greentour/search/region?regionName=" + regionName)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("find how many programs based on the region include a keyword")
    void findByOutline() throws Exception  {
        String keyword = "세계문화유산";
        webTestClient.get()
                .uri("/greentour/search/outline?keyword=" + keyword)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("find how many times such a keyword appears from the program detail")
    void findByDetail() throws Exception  {
        String keyword = "문화";
        webTestClient.get()
                .uri("/greentour/search/detail?keyword=" + keyword)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk();
    }
}