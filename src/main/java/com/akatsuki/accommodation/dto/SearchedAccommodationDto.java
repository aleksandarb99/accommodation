package com.akatsuki.accommodation.dto;

import com.akatsuki.accommodation.enums.PriceType;
import com.akatsuki.accommodation.model.Availability;
import com.akatsuki.accommodation.model.CustomPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchedAccommodationDto {
    private String name;
    private String location;
    private List<String> photographs;
    private BenefitsDto benefits;
    private int minQuests;
    private int maxQuests;
    private PriceType priceType;
    private int defaultPrice;
    private List<CustomPrice> customPrices;
    private List<Availability> availabilities;
    private int totalPrice;
}
