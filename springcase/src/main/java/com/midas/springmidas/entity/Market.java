package com.midas.springmidas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Market {

    @Id
    @SequenceGenerator(
            name = "market_sequence",
            sequenceName = "market_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "market_sequence"
    )
    private Long MarketId;

    @Column(unique = true)
    private String Code;
    private String Symbol;
    private String Name;
    private String Country;
    private String Website;

}
