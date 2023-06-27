package com.akatsuki.accommodation.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(url = "${core.services.reservation-url}", value = "reservation-feign-client")
public interface ReservationFeignClient {

    @GetMapping("/check-reservations-of-accommodation")
    boolean checkReservationsOfAccommodation(@RequestParam(name = "id", required = true) Long id,
                                             @RequestParam(name = "startDate", required = true) LocalDate startDate,
                                             @RequestParam(name = "endDate", required = true) LocalDate endDate, @RequestHeader("Authorization") final String token);
}

