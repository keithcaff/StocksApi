package com.keithcaff.stocksapi.entity;

import com.keithcaff.stocksapi.dto.StockDto;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document(collection = "user_stock")
public class UserStock {
    @Id
    private String id;

    @NotNull
    @Indexed(name = "user_id_index", direction = IndexDirection.DESCENDING, unique = true)
    private String userId;

    private Set<Stock> stocks;

    public UserStock(Set<StockDto> stockDtos) {
        this.stocks = stockDtos.stream().map(Stock::new).collect(Collectors.toSet());
    }

    public UserStock(String id, String userId, Set<Stock> stocks) {
        this.id = id;
        this.userId = userId;
        this.stocks = stocks;
    }
}



