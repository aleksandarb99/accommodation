package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.*;
import com.akatsuki.accommodation.model.Accommodation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccommodationService {

    List<Accommodation> findAll();

    Optional<Accommodation> getAccommodation(Long accommodationId);

    List<Accommodation> findPerHostAccommodations(Long hostId);

    List<SearchedAccommodationDto> searchAccommodations(String location, int numberOfGuests, LocalDate startDate, LocalDate endDate);

    void createAccommodation(AccommodationDto accommodationDto, Long hostId);

    void updateDefaultPrice(Long id, int price);

    void addCustomPrice(Long id, CustomPriceDto customPriceDto);

    void deleteCustomPrice(Long id, Long idOfPrice);

    void createAvailability(Long id, AvailabilityDto availabilityDto);

    void deleteAvailability(Long id, Long idOfAvailability);

    AvailabilityCheckResponseDto checkAvailability(Long id, AccommodationCheckDto accommodationCheckDto);

    void updateAvailability(Long id, AvailabilityUpdateDto availabilityDto, String token);

    void updateCustomPrice(Long id, CustomPriceUpdateDto customPriceDto);

    void deleteAccommodation(Long id);

    void deleteByHostId(Long hostId);
}
