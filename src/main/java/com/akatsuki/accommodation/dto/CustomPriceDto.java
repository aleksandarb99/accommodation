package com.akatsuki.accommodation.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomPriceDto {
    private LocalDate startDate;
    private LocalDate endDate;
    @Min(value = 1)
    private int price;
}
