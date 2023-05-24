package com.akatsuki.accommodation.controller;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAccommodation(@Valid @RequestBody AccommodationDto accommodationDto) {
        accommodationService.createAccommodation(accommodationDto);
    }
}
