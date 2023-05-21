package com.akatsuki.accommodation.service.impl;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.mapper.AccommodationMapper;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import com.akatsuki.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final AccommodationRepository accommodationRepository;

    @Override
    public void createAccommodation(AccommodationDto accommodationDto) {
//        TODO: Write unit and integration tests
//        TODO: Write dockerfile and docker-compose ( I am not sure )
//        TODO: After that, configure CI-CD to build and test code on creation of PR; Configure Deploy to DockerHub on merge on main
        Optional<Accommodation> optionalAccommodation = accommodationRepository
                .findByName(accommodationDto.getName());
        if (optionalAccommodation.isPresent()) {
            throw new RuntimeException(String.format("Name '%s' is already in use.", accommodationDto.getName()));
        }

        Accommodation accommodation = accommodationMapper.toEntity(accommodationDto);
        accommodationRepository.save(accommodation);
    }
}
