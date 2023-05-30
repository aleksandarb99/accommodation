package com.akatsuki.accommodation.dto;

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
    private Long accommodationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int price;
}
