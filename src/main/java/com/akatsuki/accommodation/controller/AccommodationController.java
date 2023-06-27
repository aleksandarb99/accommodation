package com.akatsuki.accommodation.controller;

import com.akatsuki.accommodation.dto.*;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.model.Availability;
import com.akatsuki.accommodation.model.CustomPrice;
import com.akatsuki.accommodation.service.AccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final JwtDecoder jwtDecoder;

    //    TODO: This should return dto
    @GetMapping
    public List<Accommodation> findAllAccommodations() {
        return accommodationService.findAll();
    }

    //    TODO: This should return dto
    @GetMapping("/{id}")
    public Optional<Accommodation> getAccommodation(@PathVariable Long id) {
        return accommodationService.getAccommodation(id);
    }

    //    TODO: This should return dto
    @GetMapping("/{id}/custom-price")
    public List<CustomPrice> getAccommodationCustomPrice(@PathVariable Long id) {
        return accommodationService.getAccommodationCustomPrice(id);
    }

    //    TODO: This should return dto
    @GetMapping("/{id}/availabilities")
    public List<Availability> getAccommodationAvailability(@PathVariable Long id) {
        return accommodationService.getAccommodationAvailability(id);
    }

    @GetMapping("/per-host")
    public List<Accommodation> findPerHostAccommodations(@RequestHeader("Authorization") final String token) {
        Long hostId = getIdFromToken(token);
        return accommodationService.findPerHostAccommodations(hostId);
    }

    //    TODO: Za sad su sva polja required, mozemo to promeniti
    @GetMapping("/search")
    public List<SearchedAccommodationDto> searchAccommodations(@RequestParam String location, @RequestParam(name = "number-of-guests") int numberOfGuests,
                                                               @RequestParam(name = "start-date") LocalDate startDate, @RequestParam(name = "end-date") LocalDate endDate) {
        return accommodationService.searchAccommodations(location, numberOfGuests, startDate, endDate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createAccommodation(@Valid @RequestBody AccommodationDto accommodationDto, @RequestHeader("Authorization") final String token) {
        Long hostId = getIdFromToken(token);
        accommodationService.createAccommodation(accommodationDto, hostId);
    }

    @PutMapping("/{id}/default-price/{price}")
    public void updateDefaultPrice(@PathVariable Long id, @PathVariable int price) {
        accommodationService.updateDefaultPrice(id, price);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteAccommodation(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/by-host-id")
    public void deleteByHostId(@RequestHeader("Authorization") final String token) {
        Long hostId = getIdFromToken(token);
        accommodationService.deleteByHostId(hostId);
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

    @PostMapping("/{id}/check-availability")
    public AvailabilityCheckResponseDto checkAccommodationAvailability(@PathVariable Long id, @Valid @RequestBody AccommodationCheckDto accommodationCheckDto) {
        return accommodationService.checkAvailability(id, accommodationCheckDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/availability")
    public void createAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityDto availabilityDto) {
        accommodationService.createAvailability(id, availabilityDto);
    }

    @PutMapping("/{id}/availability/{idOfAvailability}")
    public void updateAvailability(@PathVariable Long id, @PathVariable Long idOfAvailability, @Valid @RequestBody AvailabilityUpdateDto availabilityDto, @RequestHeader("Authorization") final String token) {
        accommodationService.updateAvailability(id, idOfAvailability, availabilityDto, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/availability/{idOfAvailability}")
    public void deleteAvailability(@PathVariable Long id, @PathVariable Long idOfAvailability) {
        accommodationService.deleteAvailability(id, idOfAvailability);
    }

    private Long getIdFromToken(String token) {
        return (Long) jwtDecoder.decode(token.split(" ")[1]).getClaims().get("id");
    }

}
