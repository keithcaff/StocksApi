package com.keithcaff.stocksapi.service;

import com.keithcaff.stocksapi.dto.StockDto;

import java.util.List;

public interface StockService {
    public List<StockDto> search(String keywords);
}
