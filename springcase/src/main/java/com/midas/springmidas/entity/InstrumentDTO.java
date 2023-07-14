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
public class InstrumentDTO {
    @Id
    private Long id;
    private String symbol;
    private String name;
    private String customName;
    private Long marketId;

    public void setId(Long id) {
        this.id = id;
    }
}
