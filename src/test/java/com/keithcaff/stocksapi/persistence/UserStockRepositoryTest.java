package com.keithcaff.stocksapi.persistence;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.Stock;
import com.keithcaff.stocksapi.entity.UserStock;
import com.keithcaff.stocksapi.repository.UserStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("persistence")
@DataMongoTest()
public class UserStockRepositoryTest {

    @Autowired
    UserStockRepository stockRepository;

    @BeforeEach
    public void clearUserStocks() {
        stockRepository.deleteAll();
    }

    @DisplayName("Given a UserStock to save, when save object using stockRepository, then object is saved")
    @Test
    public void testUserStockIsPersisted() {
        // given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(nikeStockDto));
        UserStock userStockBeforePersisted = new UserStock(stockDtos);
        userStockBeforePersisted.setUserId("user");
        // when
        stockRepository.save(userStockBeforePersisted);

        // then
        Optional<UserStock> persistedUserStock = stockRepository.findOne(Example.of(userStockBeforePersisted));
        assertTrue(persistedUserStock.isPresent());
        assertEquals(persistedUserStock.get().getUserId(),"user");
        assertEquals(persistedUserStock.get().getStocks(), userStockBeforePersisted.getStocks());
    }

    @DisplayName("Given UserStock is persisted, when update is called, then user stocks are updated")
    @Test
    public void testUpdateUserStocksModifiesStocksSet() {
        // given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        StockDto appleStockDto = new StockDto("AAPL", "Apple Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(nikeStockDto));
        UserStock userStockBeforePersisted = new UserStock(stockDtos);
        userStockBeforePersisted.setUserId("user");
        UserStock persistedUserStock = stockRepository.save(userStockBeforePersisted);

        // when
        HashSet<Stock> newFollowedStocks = new HashSet<>(Arrays.asList(new Stock(nikeStockDto),new Stock(appleStockDto)));
        stockRepository.updateUserStocks(newFollowedStocks,persistedUserStock.getId());

        // then
        Optional<UserStock> updatedStocks = stockRepository.findByUserId("user");
        assertTrue(updatedStocks.isPresent());
        assertEquals(updatedStocks.get().getStocks(), newFollowedStocks);
        assertEquals(updatedStocks.get().getStocks().size(), 2);
    }

    @DisplayName("Given UserStock is persisted, when update is called with empty Set, then user stocks are cleared")
    @Test
    public void testEmptySetClearsUserStocks() {
        // given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        StockDto appleStockDto = new StockDto("AAPL", "Apple Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet(Arrays.asList(nikeStockDto,appleStockDto));
        UserStock userStockBeforePersisted = new UserStock(stockDtos);
        userStockBeforePersisted.setUserId("user");
        UserStock persistedUserStock = stockRepository.save(userStockBeforePersisted);

        // when
        HashSet<Stock> newFollowedStocks = new HashSet();
        stockRepository.updateUserStocks(newFollowedStocks,persistedUserStock.getId());

        // then
        Optional<UserStock> updatedStocks = stockRepository.findByUserId("user");
        assertTrue(updatedStocks.isPresent());
        assertTrue(updatedStocks.get().getStocks().isEmpty());
    }
}
