package by.clevertec.house.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.util.HouseTestBuilder;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HouseControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testMultithreadedAccess() throws InterruptedException {
        int numThreads = 6;
        ExecutorService service = Executors.newFixedThreadPool(numThreads);

        try {
            for (int i = 0; i < numThreads; i++) {
                service.submit(() -> {
                    try {
                        HouseRequestDto newHouse = HouseTestBuilder.builder()
                                .build()
                                .buildHouseRequestDto();

                        ResponseEntity<Void> postResponse = restTemplate
                                .postForEntity("http://localhost:" + port + "/houses", newHouse, Void.class);
                        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

                        UUID uuid = newHouse.getUuid();
                        ResponseEntity<HouseResponseDto> getResponse = restTemplate
                                .getForEntity("http://localhost:" + port + "/houses/{uuid}",
                                        HouseResponseDto.class, uuid);
                        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
                        assertEquals(newHouse.getCity(), getResponse.getBody().getCity());

                        HouseRequestDto updatedHouse = HouseTestBuilder.builder()
                                .withCity("MINSK")
                                .build()
                                .buildHouseRequestDto();

                        restTemplate.put("http://localhost:" + port + "/houses/{uuid}", updatedHouse, uuid);

                        ResponseEntity<HouseResponseDto> updatedGetResponse = restTemplate
                                .getForEntity("http://localhost:" + port + "/houses/{uuid}",
                                        HouseResponseDto.class, uuid);
                        assertEquals(HttpStatus.OK, updatedGetResponse.getStatusCode());
                        assertEquals(updatedHouse.getCity(), updatedGetResponse.getBody().getCity());

                        restTemplate.delete("http://localhost:" + port + "/houses/{uuid}", uuid);

                        ResponseEntity<HouseResponseDto> deletedGetResponse = restTemplate
                                .getForEntity("http://localhost:" + port + "/houses/{uuid}",
                                        HouseResponseDto.class, uuid);
                        assertEquals(HttpStatus.NOT_FOUND, deletedGetResponse.getStatusCode());
                    } catch (Exception e) {
                        fail("Exception occurred during test: " + e.getMessage());
                    }
                });
            }
        } finally {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}
