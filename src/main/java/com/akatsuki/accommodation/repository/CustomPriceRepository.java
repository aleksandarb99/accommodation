package com.akatsuki.accommodation.repository;

import com.akatsuki.accommodation.model.CustomPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomPriceRepository extends JpaRepository<CustomPrice, Long> {

}
