package com.keithcaff.stocksapi.entity;

import com.keithcaff.stocksapi.dto.StockDto;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document(collection = "user_stock")

@CompoundIndex(name = "user_stock_symbol_idx", unique = true, def = "{'userId':1, 'symbol':1}")
public class UserStock {
    @Id
    private String id;

    @NotNull
    @Indexed(name = "user_id_index", direction = IndexDirection.DESCENDING, unique = true)
    private String userId;

    private Set<Stock> stocks;

    public UserStock(Set<Stock> stocks) {
        this.stocks = stocks;
    }

    public UserStock(List<StockDto> stockDtoList) {
        this.stocks = stockDtoList.stream().map(Stock::new).collect(Collectors.toSet());
    }
}



