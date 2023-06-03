package com.akatsuki.accommodation.dto;

import com.akatsuki.accommodation.enums.PriceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccommodationDto {
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotNull
    private Long hostId;
    private boolean automaticApprove;
    private List<String> photographs;
    private BenefitsDto benefits;
    @Min(value = 1)
    private int minQuests;
    private int maxQuests;
    private PriceType priceType;
    @Min(value = 1)
    private int defaultPrice;
}
