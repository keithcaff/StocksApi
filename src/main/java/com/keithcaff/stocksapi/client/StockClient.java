package com.keithcaff.stocksapi.client;

import com.keithcaff.stocksapi.dto.StockDto;
import java.util.List;

public interface StockClient {
    public List<StockDto> searchStocks(String keywords);
}
