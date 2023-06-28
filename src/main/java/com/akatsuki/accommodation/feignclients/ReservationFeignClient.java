package com.akatsuki.accommodation.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${core.services.reservation-url}", value = "reservation-feign-client")
public interface ReservationFeignClient {

    @GetMapping("/check-reservations-of-accommodation")
    boolean checkReservationsOfAccommodation(@RequestParam(name = "id") Long id,
                                             @RequestParam(name = "startDate") String startDate,
                                             @RequestParam(name = "endDate") String endDate, @RequestHeader("Authorization") final String token);
}

