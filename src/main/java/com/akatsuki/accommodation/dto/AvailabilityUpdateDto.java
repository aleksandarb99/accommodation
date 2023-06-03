package com.akatsuki.accommodation.dto;

import com.akatsuki.accommodation.enums.AvailabilityUpdateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AvailabilityUpdateDto {
    private LocalDate newDate;
    private AvailabilityUpdateType type;
}