package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.*;
import com.akatsuki.accommodation.model.Accommodation;

import java.util.List;

public interface AccommodationService {

    List<Accommodation> findAllAccommodations();

    void createAccommodation(AccommodationDto accommodationDto);

    void updateDefaultPrice(Long id, int price);

    void addCustomPrice(Long id, CustomPriceDto customPriceDto);

    void deleteCustomPrice(Long id, Long idOfPrice);

    void createAvailability(Long id, AvailabilityDto availabilityDto);

    void deleteAvailability(Long id, Long idOfAvailability);

    boolean checkAvailability(Long id, AvailabilityDto availabilityDto);

    void updateAvailability(Long id, AvailabilityUpdateDto availabilityDto);

    void updateCustomPrice(Long id, CustomPriceUpdateDto customPriceDto);
}
