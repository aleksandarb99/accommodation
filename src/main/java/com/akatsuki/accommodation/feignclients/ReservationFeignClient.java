package com.akatsuki.accommodation.feignclients;

import com.akatsuki.accommodation.dto.AccommodationInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "${core.services.reservation-url}", value = "reservation-feign-client")
public interface ReservationFeignClient {

    @GetMapping("/check-reservations-of-accommodation")
    boolean checkReservationsOfAccommodation(@RequestBody AccommodationInfoDTO accommodationInfoDTO, @RequestHeader("Authorization") final String token);
}

