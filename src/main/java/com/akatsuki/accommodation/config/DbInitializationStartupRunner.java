package com.akatsuki.accommodation.config;

import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.model.Benefits;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbInitializationStartupRunner implements ApplicationRunner {

    private final AccommodationRepository accommodationRepository;

    @Override
    public void run(ApplicationArguments args) {
        var b1 = Benefits.builder()
                .ac(true)
                .freeParkingSpace(true)
                .kitchen(true)
                .wifi(true)
                .build();
        var b2 = Benefits.builder()
                .ac(false)
                .freeParkingSpace(true)
                .kitchen(true)
                .wifi(false)
                .build();
        var b3 = Benefits.builder()
                .ac(true)
                .freeParkingSpace(false)
                .kitchen(false)
                .wifi(true)
                .build();

        var a1 = Accommodation.builder()
                .name("Vila Jovanovic")
                .location("Ulica Vladike Nikolaja 14, Beograd")
                .minQuests(2)
                .maxQuests(6)
                .benefits(b1)
                .photographs(Collections.emptyList())
                .build();
        var a2 = Accommodation.builder()
                .name("Vila Soko")
                .location("Ulica Dalmatinska 15, Beograd")
                .minQuests(1)
                .maxQuests(4)
                .benefits(b2)
                .photographs(Collections.emptyList())
                .build();
        var a3 = Accommodation.builder()
                .name("Vila Milica")
                .location("Ulica Kralja Aleksandra 20, Novi Sad")
                .minQuests(3)
                .maxQuests(6)
                .maxQuests(6)
                .benefits(b3)
                .photographs(Collections.emptyList())
                .build();

        accommodationRepository.saveAll(List.of(a1, a2, a3));
    }
}