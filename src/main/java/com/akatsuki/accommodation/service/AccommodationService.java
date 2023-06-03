package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.*;
import com.akatsuki.accommodation.model.Accommodation;

import java.time.LocalDate;
import java.util.List;

public interface AccommodationService {

    List<Accommodation> findAll();

    List<Accommodation> findPerHostAccommodations(Long hostId);

    List<SearchedAccommodationDto> searchAccommodations(String location, int numberOfGuests, LocalDate startDate, LocalDate endDate);

    void createAccommodation(AccommodationDto accommodationDto);

    void updateDefaultPrice(Long id, int price);

    void addCustomPrice(Long id, CustomPriceDto customPriceDto);

    void deleteCustomPrice(Long id, Long idOfPrice);

    void createAvailability(Long id, AvailabilityDto availabilityDto);

    void deleteAvailability(Long id, Long idOfAvailability);

    AvailabilityCheckResponseDto checkAvailability(Long id, AccommodationCheckDto accommodationCheckDto);

    void updateAvailability(Long id, AvailabilityUpdateDto availabilityDto);

    void updateCustomPrice(Long id, CustomPriceUpdateDto customPriceDto);

    void deleteAccommodation(Long id);
}
