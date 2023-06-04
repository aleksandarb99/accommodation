package com.akatsuki.accommodation.repository;

import com.akatsuki.accommodation.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    Optional<Accommodation> findByName(String name);

    List<Accommodation> findByHostId(Long hostId);
}
