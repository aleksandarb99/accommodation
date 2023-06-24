package com.akatsuki.accommodation.config;

import com.akatsuki.accommodation.enums.PriceType;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.model.Availability;
import com.akatsuki.accommodation.model.Benefits;
import com.akatsuki.accommodation.model.CustomPrice;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbInitializationStartupRunner implements ApplicationRunner {

    private final AccommodationRepository accommodationRepository;

    @Override
    public void run(ApplicationArguments args) {
        var av1 = Availability.builder()
                .startDate(LocalDate.of(2023, 6, 1))
                .endDate(LocalDate.of(2023, 8, 1))
                .build();

        var cp1 = CustomPrice.builder()
                .startDate(LocalDate.of(2023, 7, 1))
                .endDate(LocalDate.of(2023, 8, 1))
                .price(20)
                .build();

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
                .hostId(1L)
                .automaticApprove(true)
                .minQuests(2)
                .maxQuests(6)
                .benefits(b1)
                .photographs(Collections.emptyList())
                .priceType(PriceType.FIXED_PER_NIGHT)
                .defaultPrice(100)
                .availabilities(Collections.emptyList())
                .customPrices(Collections.emptyList())
                .build();
        var a2 = Accommodation.builder()
                .name("Vila Soko")
                .location("Ulica Dalmatinska 15, Beograd")
                .hostId(1L)
                .automaticApprove(true)
                .minQuests(1)
                .maxQuests(4)
                .benefits(b2)
                .photographs(Collections.emptyList())
                .priceType(PriceType.FIXED_PER_NIGHT)
                .defaultPrice(80)
                .availabilities(Collections.emptyList())
                .customPrices(Collections.emptyList())
                .build();
        var a3 = Accommodation.builder()
                .name("Vila Milica")
                .location("Ulica Kralja Aleksandra 20, Novi Sad")
                .hostId(2L)
                .automaticApprove(true)
                .minQuests(3)
                .maxQuests(6)
                .maxQuests(6)
                .benefits(b3)
                .photographs(Collections.emptyList())
                .priceType(PriceType.PER_PERSON_PER_NIGHT)
                .defaultPrice(15)
                .availabilities(List.of(av1))
                .customPrices(List.of(cp1))
                .build();

        accommodationRepository.saveAll(List.of(a1, a2, a3));
    }
}