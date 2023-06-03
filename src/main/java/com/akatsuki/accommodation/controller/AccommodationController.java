package com.akatsuki.accommodation.controller;

import com.akatsuki.accommodation.dto.*;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public List<Accommodation> findAllAccommodations() {
        return accommodationService.findAll();
    }

    @GetMapping("/search")
    public List<SearchedAccommodationDto> searchAccommodations(@RequestParam String location, @RequestParam(name = "number-of-guests") int numberOfGuests,
                                                               @RequestParam(name = "start-date") LocalDate startDate, @RequestParam(name = "end-date") LocalDate endDate) {
        return accommodationService.searchAccommodations(location, numberOfGuests, startDate, endDate);
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
    @PostMapping("/{id}/custom-price")
    public void addCustomPrice(@PathVariable Long id, @Valid @RequestBody CustomPriceDto customPriceDto) {
        accommodationService.addCustomPrice(id, customPriceDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/custom-price/{id}")
    public void updateCustomPrice(@PathVariable Long id, @Valid @RequestBody CustomPriceUpdateDto customPriceDto) {
        accommodationService.updateCustomPrice(id, customPriceDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/custom-price/{idOfPrice}")
    public void deleteCustomPrice(@PathVariable Long id, @PathVariable Long idOfPrice) {
        accommodationService.deleteCustomPrice(id, idOfPrice);
    }

    @GetMapping("/{id}/availability")
    public AvailabilityCheckResponseDto checkAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityDto availabilityDto) {
        boolean available = accommodationService.checkAvailability(id, availabilityDto);
        return new AvailabilityCheckResponseDto(available);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/availability")
    public void createAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityDto availabilityDto) {
        accommodationService.createAvailability(id, availabilityDto);
    }

    @PutMapping("/availability/{id}")
    public void updateAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityUpdateDto availabilityDto) {
        accommodationService.updateAvailability(id, availabilityDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/availability/{idOfAvailability}")
    public void deleteAvailability(@PathVariable Long id, @PathVariable Long idOfAvailability) {
        accommodationService.deleteAvailability(id, idOfAvailability);
    }

}
