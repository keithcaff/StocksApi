package com.keithcaff.stocksapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor @Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDto implements Serializable {

    private String symbol;
    private String name;
    private String region;
    private String currency;

    public StockDto(StockQueryDto stockQueryDto) {
        this(stockQueryDto.getSymbol(),stockQueryDto.getName(),stockQueryDto.getRegion(),stockQueryDto.getCurrency());
    }

    public StockDto(String symbol, String name, String region, String currency) {
        this.symbol = symbol;
        this.name = name;
        this.region = region;
        this.currency = currency;
    }
}

