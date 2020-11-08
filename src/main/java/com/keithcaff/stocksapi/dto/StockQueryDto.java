package com.keithcaff.stocksapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class StockQueryDto implements Serializable {

    private String symbol;
    private String name;
    private String region;
    private String currency;

    @JsonProperty("1. symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("2. name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("4. region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("8. currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
