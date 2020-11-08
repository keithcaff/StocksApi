package com.keithcaff.stocksapi.service;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.UserStock;

import java.util.List;

public interface StockService {
    List<StockDto> search(String keywords);
    UserStock createUserStock(List<StockDto> stockDto);
}
