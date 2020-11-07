package com.keithcaff.stocksapi.controller;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/stocks")
@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @ResponseBody
    @GetMapping()
    public List<StockDto> getStocks() {
        return stockService.search("tesco");
    }
}
