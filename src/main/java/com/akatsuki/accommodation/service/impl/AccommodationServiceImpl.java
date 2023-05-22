package com.akatsuki.accommodation.service.impl;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import com.akatsuki.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createAccommodation(AccommodationDto accommodationDto) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository
                .findByName(accommodationDto.getName());
        if (optionalAccommodation.isPresent()) {
            throw new RuntimeException(String.format("Name '%s' is already in use.", accommodationDto.getName()));
        }

        Accommodation accommodation = modelMapper.map(accommodationDto, Accommodation.class);
        accommodationRepository.save(accommodation);
    }
}
