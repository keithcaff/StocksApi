package com.keithcaff.stocksapi.service;

import com.keithcaff.stocksapi.dto.StockDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Override
    public List<StockDto> search(String keywords) {
        List<StockDto> stockDtos = new ArrayList<>();
        StockDto stock = new StockDto("TSCDY","Tesco PLC", "United States", "USD");
        stockDtos.add(stock);
        return stockDtos;
    }
}
