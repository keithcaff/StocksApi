package com.keithcaff.stocksapi.controller;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.dto.UserStockDto;
import com.keithcaff.stocksapi.entity.UserStock;
import com.keithcaff.stocksapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @ResponseBody
    @GetMapping("/stocks/search")
    public List<StockDto> getStocks(@RequestParam String keywords) {
        return stockService.search(keywords);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/stocks")
    public UserStockDto followStocks(@RequestBody @Valid Set<StockDto> stockDtos) {
        UserStock userStock = stockService.createUserStock(stockDtos);
        return new UserStockDto(userStock);
    }

    @ResponseBody
    @GetMapping("/user/stocks")
    public UserStockDto getUserStocks(Principal principal) {
        Optional<UserStock> userStock = stockService.getUserStocks(principal.getName());
        if (userStock.isPresent()) {
            return new UserStockDto(userStock.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not follow any stocks");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/user/stocks/{userStocksId}")
    public UserStockDto updateStocks(@RequestBody @Valid Set<StockDto> stockDtos, @PathVariable("userStocksId") String userStocksId) {
        Optional<UserStock> userStock = stockService.updateUserStocks(stockDtos,userStocksId);
        if (userStock.isPresent()) {
            return new UserStockDto(userStock.get());
        }
        String errorMessage = String.format("User does not have any Stocks with id %s", userStocksId);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
    }
}
