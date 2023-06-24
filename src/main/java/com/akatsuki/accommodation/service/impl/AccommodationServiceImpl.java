package com.akatsuki.accommodation.service.impl;

import com.akatsuki.accommodation.dto.*;
import com.akatsuki.accommodation.enums.AvailabilityUpdateType;
import com.akatsuki.accommodation.enums.PriceType;
import com.akatsuki.accommodation.exception.BadRequestException;
import com.akatsuki.accommodation.feignclients.ReservationFeignClient;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.model.Availability;
import com.akatsuki.accommodation.model.CustomPrice;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import com.akatsuki.accommodation.repository.AvailabilityRepository;
import com.akatsuki.accommodation.repository.CustomPriceRepository;
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
    private final CustomPriceRepository customPriceRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ModelMapper modelMapper;
    private final ReservationFeignClient reservationFeignClient;

    @Override
    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Optional<Accommodation> getAccommodation(Long accommodationId) {
        return accommodationRepository.findById(accommodationId);
    }

    @Override
    public List<Accommodation> findPerHostAccommodations(Long hostId) {
        return accommodationRepository.findByHostId(hostId);
    }

    @Override
    public List<SearchedAccommodationDto> searchAccommodations(String location, int numberOfGuests, LocalDate startDate, LocalDate endDate) {
        if (numberOfGuests <= 0) {
            throw new BadRequestException("Number of quests must be positive.");
        }
        LocalDate now = LocalDate.now();
        if (!startDate.isAfter(now) || !endDate.isAfter(now)) {
            throw new BadRequestException("Dates must be in future.");
        }

        List<Accommodation> accommodations = accommodationRepository.findAll();

        accommodations = accommodations.stream().filter(
                a -> a.getLocation().toLowerCase().startsWith(location.toLowerCase())).toList();
        accommodations = accommodations.stream().filter(
                a -> a.getMinQuests() <= numberOfGuests && a.getMaxQuests() >= numberOfGuests).toList();
        accommodations = accommodations.stream().filter(
                a -> checkAvailabilityForEveryDay(startDate, endDate, a)).toList();

        List<SearchedAccommodationDto> accommodationDtos = accommodations.stream().map(
                a -> modelMapper.map(
                        a, SearchedAccommodationDto.class)).toList();
        accommodationDtos.forEach(a -> a.setTotalPrice(calculateTotalCost(a, numberOfGuests, startDate, endDate)));
        return accommodationDtos;
    }

    @Override
    public void deleteAccommodation(Long id) {
        Accommodation a = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));
        accommodationRepository.delete(a);
    }

    private int calculateTotalCost(SearchedAccommodationDto accommodationDto, int numberOfGuests, LocalDate startDate, LocalDate endDate) {
        int totalCost = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            totalCost += calculateCostForDate(accommodationDto, numberOfGuests, date);
        }
        return totalCost;
    }

    private int calculateCostForDate(SearchedAccommodationDto accommodationDto, int numberOfGuests, LocalDate date) {
        int price = accommodationDto.getDefaultPrice();
        for (CustomPrice cp : accommodationDto.getCustomPrices()) {
            if (date.isAfter(cp.getStartDate()) && date.isBefore(cp.getEndDate())) {
                price = cp.getPrice();
            }
        }
        if (accommodationDto.getPriceType().equals(PriceType.PER_PERSON_PER_NIGHT)) {
            price *= numberOfGuests;
        }
        return price;
    }

    @Override
    public void createAccommodation(AccommodationDto accommodationDto, Long hostId) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository
                .findByName(accommodationDto.getName());
        if (optionalAccommodation.isPresent()) {
            throw new BadRequestException(String.format("Name '%s' is already in use.", accommodationDto.getName()));
        }
        Accommodation accommodation = modelMapper.map(accommodationDto, Accommodation.class);
        accommodation.setHostId(hostId);
        accommodation.setCustomPrices(Collections.emptyList());
        accommodation.setAvailabilities(Collections.emptyList());
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
    public void addCustomPrice(Long id, CustomPriceDto customPriceDto) {
        if (customPriceDto.getPrice() <= 0) {
            throw new BadRequestException("Price is not valid.");
        }

        if (!customPriceDto.getStartDate().isBefore(customPriceDto.getEndDate())) {
            throw new BadRequestException("End date is before start date.");
        }

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
    public void updateCustomPrice(Long id, CustomPriceUpdateDto customPriceDto) {
        CustomPrice cp = customPriceRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Custom price with id '%s' does not exist.", id)));

        cp.setPrice(customPriceDto.getPrice());
        customPriceRepository.save(cp);
    }

    @Override
    public void deleteCustomPrice(Long id, Long idOfPrice) {
        Accommodation a = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));

        Optional<CustomPrice> customPriceOptional = a.getCustomPrices().stream().filter(cp -> cp.getId().equals(idOfPrice)).findFirst();
        if (customPriceOptional.isEmpty()) {
            throw new BadRequestException(String.format("Custom rice with id '%s' does not exist on accommodation with id '%s'.", idOfPrice, id));
        }

        CustomPrice priceForDeletion = customPriceOptional.get();
        a.getCustomPrices().remove(priceForDeletion);

        accommodationRepository.save(a);
        customPriceRepository.delete(priceForDeletion);
    }

    @Override
    public AvailabilityCheckResponseDto checkAvailability(Long id, AccommodationCheckDto dto) {
        if (!dto.getStartDate().isBefore(dto.getEndDate())) {
            throw new BadRequestException("End date is before start date.");
        }

        Accommodation accommodation = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));

        boolean available = checkAvailabilityForEveryDay(dto.getStartDate(), dto.getEndDate(), accommodation);
        int totalCost = 0;
        if (available) {
            totalCost = calculateTotalCost(modelMapper.map(accommodation, SearchedAccommodationDto.class),
                    dto.getNumberOfGuests(), dto.getStartDate(), dto.getEndDate());
        }
        return AvailabilityCheckResponseDto.builder()
                .id(id)
                .available(available)
                .totalCost(totalCost)
                .automaticApprove(accommodation.isAutomaticApprove())
                .build();
    }

    private boolean checkAvailabilityForEveryDay(LocalDate startDate, LocalDate endDate, Accommodation accommodation) {
        if (accommodation.getAvailabilities().isEmpty()) {
            return false;
        }
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;
            List<Availability> availabilities = accommodation.getAvailabilities().stream().filter(
                    currectAvailability -> currectAvailability.getStartDate().isAfter(currentDate)
                            || currectAvailability.getEndDate().isBefore(currentDate)).toList();
            if (availabilities.size() == accommodation.getAvailabilities().size()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void createAvailability(Long id, AvailabilityDto availabilityDto) {
        if (!availabilityDto.getStartDate().isBefore(availabilityDto.getEndDate())) {
            throw new BadRequestException("End date is before start date.");
        }

        Accommodation a = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));

        LocalDate startDate = availabilityDto.getStartDate();
        LocalDate endDate = availabilityDto.getEndDate();

        for (Availability availability : a.getAvailabilities()) {
            if (!startDate.isAfter(availability.getStartDate()) && !endDate.isBefore(availability.getStartDate())) {
                throw new BadRequestException("Range is not valid.");
            }
            if (!endDate.isBefore(availability.getEndDate()) && !startDate.isAfter(availability.getEndDate())) {
                throw new BadRequestException("Range is not valid.");
            }
        }

        Availability availability = modelMapper.map(availabilityDto, Availability.class);
        a.getAvailabilities().add(availability);

        accommodationRepository.save(a);
    }

    @Override
    public void updateAvailability(Long id, AvailabilityUpdateDto availabilityDto, String token) {

        Availability a = availabilityRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Availability with id '%s' does not exist.", id)));

        AvailabilityUpdateType type = availabilityDto.getType();
        LocalDate newDate = availabilityDto.getNewDate();

        if (type.equals(AvailabilityUpdateType.START_DATE)) {
            if (!newDate.isBefore(a.getEndDate())) {
                throw new BadRequestException("New date is not before end date.");
            }
        } else {
            if (!newDate.isAfter(a.getStartDate())) {
                throw new BadRequestException("New date is not after start date.");
            }
        }

        LocalDate newStartDate;
        LocalDate newEndDate;

        if (type.equals(AvailabilityUpdateType.START_DATE)) {
            newStartDate = newDate;
            newEndDate = a.getEndDate();
        } else {
            newStartDate = a.getStartDate();
            newEndDate = newDate;
        }

        AccommodationInfoDTO accommodationInfoDTO = AccommodationInfoDTO.builder()
                .accommodationId(id)
                .startDate(newStartDate)
                .endDate(newEndDate)
                .build();

        boolean reservationExistence = reservationFeignClient.checkReservationsOfAccommodation(accommodationInfoDTO, token);

        if (reservationExistence) {
            throw new BadRequestException("Availability cannot be updated duo to existence of reservation.");
        }

        a.setStartDate(newStartDate);
        a.setEndDate(newEndDate);

        availabilityRepository.save(a);
    }

    @Override
    public void deleteAvailability(Long id, Long idOfAvailability) {
        Accommodation a = accommodationRepository
                .findById(id).orElseThrow(
                        () -> new BadRequestException(String.format("Accommodation with id '%s' does not exist.", id)));

        Optional<Availability> availabilityOptional = a.getAvailabilities().stream().filter(av -> av.getId().equals(idOfAvailability)).findFirst();
        if (availabilityOptional.isEmpty()) {
            throw new BadRequestException(String.format("Availability with id '%s' does not exist on accommodation with id '%s'.", idOfAvailability, id));
        }

        Availability availabilityForDeletion = availabilityOptional.get();
        a.getAvailabilities().remove(availabilityForDeletion);

        accommodationRepository.save(a);
        availabilityRepository.delete(availabilityForDeletion);
    }

}
