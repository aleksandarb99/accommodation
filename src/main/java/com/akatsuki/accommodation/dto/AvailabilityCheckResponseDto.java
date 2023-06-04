package com.akatsuki.accommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AvailabilityCheckResponseDto {
    private Long id;
    private boolean available;
    private int totalCost;
    private boolean automaticApprove;
}
