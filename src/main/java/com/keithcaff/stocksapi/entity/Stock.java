package com.keithcaff.stocksapi.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
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
}
