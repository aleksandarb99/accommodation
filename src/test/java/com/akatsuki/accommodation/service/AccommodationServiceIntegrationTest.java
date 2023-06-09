package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.dto.BenefitsDto;
import com.akatsuki.accommodation.exception.BadRequestException;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

@SpringBootTest
@Testcontainers(parallel = true)
class AccommodationServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.username", db::getUsername);
        registry.add("spring.datasource.password", db::getPassword);
    }

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Test
    void createAccommodationTestRuntimeException() {
        // Given
        Long hostId = 123L;
        AccommodationDto accommodationDto = new AccommodationDto();
        accommodationDto.setName("Vila Jovanovic");

        // When - Then
        Assertions.assertThrows(BadRequestException.class, () -> accommodationService.createAccommodation(accommodationDto, hostId));
    }

    @Test
    void createAccommodationTest() {
        // Given
        Long hostId = 123L;
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
        accommodationService.createAccommodation(accommodationDto, hostId);

        // Then
        Assertions.assertEquals(4, accommodationRepository.count());
        Assertions.assertTrue(accommodationRepository.findByName(accommodationDto.getName()).isPresent());
    }

}
