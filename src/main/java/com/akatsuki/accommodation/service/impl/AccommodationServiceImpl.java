package com.akatsuki.accommodation.service.impl;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.dto.CustomPriceDto;
import com.akatsuki.accommodation.exception.BadRequestException;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.model.CustomPrice;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import com.akatsuki.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Accommodation> findAllAccommodations() {
        return accommodationRepository.findAll();
    }

    @Override
    public void createAccommodation(AccommodationDto accommodationDto) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository
                .findByName(accommodationDto.getName());
        if (optionalAccommodation.isPresent()) {
            throw new BadRequestException(String.format("Name '%s' is already in use.", accommodationDto.getName()));
        }
        Accommodation accommodation = modelMapper.map(accommodationDto, Accommodation.class);
        accommodation.setCustomPrices(Collections.emptyList());
        accommodationRepository.save(accommodation);
    }

    @Override
    public void updateDefaultPrice(Long id, int price) {
        if (price <= 0) {
            throw new BadRequestException("Price is not valid.");
        }

        Accommodation accommodation = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));

        accommodation.setDefaultPrice(price);
        accommodationRepository.save(accommodation);
    }

    @Override
    public void addCustomPrice(CustomPriceDto customPriceDto) {
        if (customPriceDto.getPrice() <= 0) {
            throw new BadRequestException("Price is not valid.");
        }

        if (!customPriceDto.getStartDate().isBefore(customPriceDto.getEndDate())) {
            throw new BadRequestException("End date is before start date.");
        }

        Long id = customPriceDto.getAccommodationId();
        Accommodation a = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));

        LocalDate startDate = customPriceDto.getStartDate();
        LocalDate endDate = customPriceDto.getEndDate();

        for (CustomPrice cp : a.getCustomPrices()) {
            if (!startDate.isAfter(cp.getStartDate()) && !endDate.isBefore(cp.getStartDate())) {
                throw new BadRequestException("Range is not valid.");
            }
            if (!endDate.isBefore(cp.getEndDate()) && !startDate.isAfter(cp.getEndDate())) {
                throw new BadRequestException("Range is not valid.");
            }
        }

        CustomPrice customPrice = modelMapper.map(customPriceDto, CustomPrice.class);
        a.getCustomPrices().add(customPrice);
        accommodationRepository.save(a);
    }

    @Override
    public void deleteCustomPrice(Long id) {
        Accommodation a = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));
        accommodationRepository.delete(a);
    }
}
