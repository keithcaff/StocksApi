package com.keithcaff.stocksapi.controller;

import com.keithcaff.stocksapi.client.StockClient;
import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stocks")
@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @ResponseBody
    @GetMapping("/search")
    public List<StockDto> getStocks(@RequestParam String keywords) {
        return stockService.search(keywords);
    }
}
