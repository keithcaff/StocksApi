package com.keithcaff.stocksapi.controller;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.UserStock;
import com.keithcaff.stocksapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/follow")
    public UserStock followStocks(@RequestBody @Valid List<StockDto> stockDtos) {
        return stockService.createUserStock(stockDtos);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userStocksId}")
    public void updateStocks(@RequestBody @Valid List<StockDto> stockDtos, @PathVariable("userStocksId") String userStocksId) {
        //TODO: implement this
    }

}
