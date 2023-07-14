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
public class Instrument {

    @Id
    private Long id;

    @Column(unique = true)
    private String symbol;

    private String name;

    private String customName;

    //FetchType is eager because markets are of small size
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "market_id",
            referencedColumnName = "MarketId"
    )
    private Market market;

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }
}
