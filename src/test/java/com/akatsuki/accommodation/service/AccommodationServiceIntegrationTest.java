package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.dto.BenefitsDto;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

@SpringBootTest
@Testcontainers(parallel = true)
public class AccommodationServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.username", db::getUsername);
        registry.add("spring.datasource.password", db::getPassword);
    }

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Test
    public void createAccommodationTestRuntimeException() {
        // Given
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setName("Vila Jovanovic");

        // When - Then
        Assertions.assertThrows(RuntimeException.class, () -> accommodationService.createAccommodation(accommodationDto));
    }

    @Test
    public void createAccommodationTest() {
        // Given
        BenefitsDto benefitsDto = BenefitsDto.builder()
                .ac(true)
                .freeParkingSpace(false)
                .kitchen(false)
                .wifi(true)
                .build();
        AccommodationDto accommodationDto = AccommodationDto.builder()
                .name("Sobe Jelena")
                .location("Ulica Kralja Petra I, Nis")
                .minQuests(3)
                .maxQuests(9)
                .benefits(benefitsDto)
                .photographs(Collections.emptyList())
                .build();

        // When
        accommodationService.createAccommodation(accommodationDto);

        // Then
        Assertions.assertEquals(5, accommodationRepository.count());
        Assertions.assertTrue(accommodationRepository.findByName(accommodationDto.getName()).isPresent());
    }

}
