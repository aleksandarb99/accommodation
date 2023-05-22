package com.akatsuki.accommodation.service;

import com.akatsuki.accommodation.dto.AccommodationDto;
import com.akatsuki.accommodation.model.Accommodation;
import com.akatsuki.accommodation.repository.AccommodationRepository;
import com.akatsuki.accommodation.service.impl.AccommodationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccommodationServiceTest {

    @Mock
    private AccommodationRepository accommodationRepositoryMock;
    @Mock
    private ModelMapper modelMapperMock;
    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Test
    public void createAccommodationTestRuntimeException() {
        // Given
        AccommodationDto accommodationDto = new AccommodationDto();
        Optional<Accommodation> optionalAccommodation = Optional.of(new Accommodation());

        when(accommodationRepositoryMock.findByName(any())).thenReturn(optionalAccommodation);

        // When - Then
        Assertions.assertThrows(RuntimeException.class, () -> accommodationService.createAccommodation(accommodationDto));
    }

    @Test
    public void createAccommodationTest() {
        // Given
        AccommodationDto accommodationDto = new AccommodationDto();
        Accommodation accommodation = new Accommodation();

        when(accommodationRepositoryMock.findByName(any())).thenReturn(Optional.empty());
        when(modelMapperMock.map(any(AccommodationDto.class), eq(Accommodation.class))).thenReturn(accommodation);

        // When
        accommodationService.createAccommodation(accommodationDto);

        // Then
        verify(accommodationRepositoryMock, times(1)).save(any(Accommodation.class));
    }
}
