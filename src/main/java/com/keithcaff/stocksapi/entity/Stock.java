package com.keithcaff.stocksapi.entity;

import com.keithcaff.stocksapi.dto.StockDto;
import lombok.*;

import javax.validation.constraints.NotNull;
@ToString
@Getter @Setter
@EqualsAndHashCode @NoArgsConstructor
public class Stock {
    @NotNull
    private String symbol;
    @NotNull
    private String name;
    @NotNull
    private String region;
    @NotNull
    private String currency;

    public Stock(@NotNull String symbol, @NotNull String name, @NotNull String region, @NotNull String currency) {
        this.symbol = symbol;
        this.name = name;
        this.region = region;
        this.currency = currency;
    }

    public Stock(StockDto stockDto) {
        this(stockDto.getSymbol(),stockDto.getName(),stockDto.getRegion(),stockDto.getCurrency());
    }
}
