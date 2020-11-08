package com.keithcaff.stocksapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor @Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDto implements Serializable {

    @NotBlank
    private String symbol;
    @NotBlank
    private String name;
    @NotBlank
    private String region;
    @NotBlank
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

