package com.keithcaff.stocksapi.service;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.UserStock;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StockService {
    List<StockDto> search(String keywords);
    UserStock createUserStock(Set<StockDto> stockDtos);
    Optional<UserStock> getUserStocks(String userId);
    Optional<UserStock> updateUserStocks(Set<StockDto> stockDtos, String userStockId);
}
