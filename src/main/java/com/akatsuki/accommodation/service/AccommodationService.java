package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.dto.CustomPriceDto;
import com.akatsuki.accommodation.model.Accommodation;

import java.util.List;

public interface AccommodationService {

    List<Accommodation> findAllAccommodations();

    void createAccommodation(AccommodationDto accommodationDto);

    void updateDefaultPrice(Long id, int price);

    void addCustomPrice(CustomPriceDto customPriceDto);

    void deleteCustomPrice(Long id);
}
