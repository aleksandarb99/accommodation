package com.akatsuki.accommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BenefitsDto {
    private boolean wifi;
    private boolean kitchen;
    private boolean ac;
    private boolean freeParkingSpace;
}
