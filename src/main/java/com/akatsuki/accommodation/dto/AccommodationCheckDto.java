package com.akatsuki.accommodation.dto;

import jakarta.validation.constraints.Future;
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
public class AccommodationCheckDto {
    @Min(value = 1)
    private int numberOfGuests;
    @Future
    private LocalDate startDate;
    @Future
    private LocalDate endDate;
}
