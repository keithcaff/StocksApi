package com.keithcaff.stocksapi.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor

public class StockDto implements Serializable {

    public StockDto(String symbol, String name, String region, String currency) {
        this.symbol = symbol;
        this.name = name;
        this.region = region;
        this.currency = currency;
    }

    private String symbol;
    private String name;
    private String region;
    private String currency;

}

