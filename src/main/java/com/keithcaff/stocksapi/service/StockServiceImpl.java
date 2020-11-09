package com.keithcaff.stocksapi.service;

import com.keithcaff.stocksapi.client.StockClient;
import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.Stock;
import com.keithcaff.stocksapi.entity.UserStock;
import com.keithcaff.stocksapi.exception.StockServiceException;
import com.keithcaff.stocksapi.repository.UserStockRepository;
import com.mongodb.MongoWriteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

    private final StockClient stockClient;
    private final UserStockRepository userStockRepository;

    @Override
    public List<StockDto> search(String keywords) {
        return stockClient.searchStocks(keywords);
    }

    @Override
    public UserStock createUserStock(Set<StockDto> stockDtos) {
        UserStock userStock = new UserStock(stockDtos);
        UserStock persistedStock;
        try {
            persistedStock = userStockRepository.save(userStock);
        }
        catch(MongoWriteException exception) {
            log.error("Unable to create new UserStock entry. {}", exception.getLocalizedMessage());
            throw new StockServiceException(exception.getMessage());
        }
        log.debug("Created new userStock {}", persistedStock);
        return persistedStock;
    }

    @Override
    public Optional<UserStock> getUserStocks(String userId) {
        return userStockRepository.findByUserId(userId);
    }

    @Override
    public Optional<UserStock> updateUserStocks(Set<StockDto> stockDtos, String userStockId) {
        Set<Stock> stocks = stockDtos.stream().map(Stock::new).collect(Collectors.toSet());
        return userStockRepository.updateUserStocks(stocks,userStockId);
    }
}
