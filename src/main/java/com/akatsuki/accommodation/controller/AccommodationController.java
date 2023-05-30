package com.akatsuki.accommodation.controller;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.dto.CustomPriceDto;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public List<Accommodation> findAllAccommodations() {
        return accommodationService.findAllAccommodations();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAccommodation(@Valid @RequestBody AccommodationDto accommodationDto) {
        accommodationService.createAccommodation(accommodationDto);
    }

    @PutMapping("/{id}/default-price/{price}")
    public void updateDefaultPrice(@PathVariable Long id, @PathVariable int price) {
        accommodationService.updateDefaultPrice(id, price);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/custom-price")
    public void addCustomPrice(@Valid @RequestBody CustomPriceDto customPriceDto) {
        accommodationService.addCustomPrice(customPriceDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/custom-price/{id}")
    public void deleteCustomPrice(@PathVariable Long id) {
        accommodationService.deleteCustomPrice(id);
    }

}
