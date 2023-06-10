package com.akatsuki.accommodation;

import com.akatsuki.accommodation.feignclients.ReservationFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {ReservationFeignClient.class})
public class AccommodationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccommodationApplication.class, args);
    }

}
