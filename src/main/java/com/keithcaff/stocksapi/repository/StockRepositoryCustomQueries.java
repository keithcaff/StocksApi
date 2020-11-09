package com.keithcaff.stocksapi.repository;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.Stock;
import com.keithcaff.stocksapi.entity.UserStock;

import java.util.Optional;
import java.util.Set;

public interface StockRepositoryCustomQueries {
    Optional<UserStock> updateUserStocks(Set<Stock> updatedStocks, String userStockId);
}
