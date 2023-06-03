package com.akatsuki.accommodation.model;

import com.akatsuki.accommodation.enums.PriceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "accommodations")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private List<String> photographs;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Benefits benefits;
    private int minQuests;
    private int maxQuests;
    @Enumerated(EnumType.STRING)
    private PriceType priceType;
    private int defaultPrice;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomPrice> customPrices;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Availability> availabilities;
}
