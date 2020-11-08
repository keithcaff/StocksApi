package com.keithcaff.stocksapi.service;

import com.keithcaff.stocksapi.client.StockClient;
import com.keithcaff.stocksapi.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

    private final StockClient stockClient;

    @Override
    public List<StockDto> search(String keywords) {
        return stockClient.searchStocks(keywords);
    }
}
