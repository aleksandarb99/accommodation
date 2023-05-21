package com.akatsuki.accommodation.mapper;

import com.akatsuki.accommodation.dto.BenefitsDto;
import com.akatsuki.accommodation.model.Benefits;
import org.mapstruct.Mapper;

@Mapper
public interface BenefitsMapper {
    BenefitsDto toDto(Benefits benefits);

    Benefits toEntity(BenefitsDto benefitsDto);
}