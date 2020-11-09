package com.keithcaff.stocksapi.dto;

import com.keithcaff.stocksapi.entity.UserStock;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserStockDto implements Serializable {
    @NotEmpty
    private String id;
    @NotNull
    private Set<StockDto> stocks;

    public UserStockDto(UserStock entity) {
        this.id = entity.getId();
        this.stocks = entity.getStocks().stream().map(StockDto::new).collect(Collectors.toSet());
    }
}
