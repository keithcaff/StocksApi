package com.keithcaff.stocksapi.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document(collection = "user_stock")
@CompoundIndex(name = "user_stock_symbol_idx", unique = true, def = "{'userId':1, 'symbol':1}")
public class UserStock {
    @Id
    private String id;
    @NotNull
    private String userId;
    private Set<Stock> stocks;
}



