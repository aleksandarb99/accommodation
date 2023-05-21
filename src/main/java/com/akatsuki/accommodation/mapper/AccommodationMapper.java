package com.akatsuki.accommodation.mapper;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.model.Accommodation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccommodationMapper {
    AccommodationDto toDto(Accommodation accommodation);

    Accommodation toEntity(AccommodationDto accommodationDto);
}