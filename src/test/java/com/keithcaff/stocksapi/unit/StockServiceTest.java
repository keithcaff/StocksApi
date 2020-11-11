package com.keithcaff.stocksapi.unit;

import com.keithcaff.stocksapi.client.StockClient;
import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.Stock;
import com.keithcaff.stocksapi.entity.UserStock;
import com.keithcaff.stocksapi.exception.StockConflictException;
import com.keithcaff.stocksapi.repository.UserStockRepository;
import com.keithcaff.stocksapi.service.StockService;
import com.keithcaff.stocksapi.service.StockServiceImpl;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    private StockService stockService;

    @Mock
    private UserStockRepository stockRepository;

    @Mock
    private StockClient stockClient;

    @BeforeEach
    void init() {
        stockService = new StockServiceImpl(stockClient, stockRepository);
    }

    @Test
    @DisplayName("Should invoke stock client when search stocks is called")
    public void searchStocksTest() {
        //given
        String keywords = "apple";
        //when
        stockService.search(keywords);
        //then
        verify(stockClient, times(1)).searchStocks(keywords);
    }

    @DisplayName("createUserStock should catch DuplicateKeyException and throw StockConflictException")
    @Test
    public void duplicateKeyExceptionHandledTest() {
        //given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(nikeStockDto));
        when(stockRepository.save(any())).thenThrow(new DuplicateKeyException("error"));
        //when
        Throwable exception = assertThrows(StockConflictException.class, () -> {
            stockService.createUserStock(stockDtos);
        });
        //then
        assertEquals("User Stock resource already exists", exception.getMessage());
        verify(stockRepository, times(1)).save(any());
    }

    @DisplayName("Should save UserStock entity in repository")
    @Test
    public void shouldSaveNewUserStocks() {
        //given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(nikeStockDto));

        when(stockRepository.save(any()))
                .thenReturn(new UserStock("1", "user",
                        new HashSet<>(Arrays.asList(new Stock("NKE", "NIKE Inc.", "United States", "USD")))));
        //when
        stockService.createUserStock(stockDtos);
        //then
        verify(stockRepository, times(1)).save(new UserStock(stockDtos));
    }

    @DisplayName("getUserStocks should get all user stocks from stock repository")
    @Test
    public void shouldGetUserStocks() {
        //given
        Stock nikeStock = new Stock("NKE", "NIKE Inc.", "United States", "USD");

        when(stockRepository.findByUserId("user")).thenReturn(
                Optional.of(new UserStock("1", "user", new HashSet<>(Arrays.asList(nikeStock)))));
        //when
        Optional<UserStock> allStocks = stockService.getUserStocks("user");
        //then
        verify(stockRepository, times(1)).findByUserId("user");
        assertTrue(allStocks.isPresent());
    }

    @DisplayName("getUserStocks should return empty optional when user does follow any stocks")
    @Test
    public void shouldReturnEmptyOptional() {
        //given
        when(stockRepository.findByUserId("user")).thenReturn(Optional.empty());
        //when
        Optional<UserStock> allStocks = stockService.getUserStocks("user");
        //then
        verify(stockRepository, times(1)).findByUserId("user");
        assertFalse(allStocks.isPresent());
    }

    @DisplayName("Should update user's current stocks in repository")
    @Test
    public void shouldUpdateUsersStocksInRepository() {
        //given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        String userStockId = "123456xyz";

        UserStock updatedUserStocks = new UserStock(new HashSet<>(Arrays.asList(nikeStockDto)));
        updatedUserStocks.setId(userStockId);
        updatedUserStocks.setUserId("user");
        when(stockRepository.updateUserStocks(updatedUserStocks.getStocks(), userStockId)).
                thenReturn(Optional.of(updatedUserStocks));

        //when
        Optional<UserStock> updateStocksFromRepo = stockService.updateUserStocks(new HashSet<>(Arrays.asList(nikeStockDto)), userStockId);

        //then
        verify(stockRepository, times(1)).updateUserStocks(updatedUserStocks.getStocks(), userStockId);
        assertTrue(updateStocksFromRepo.isPresent());
        assertEquals(1, updateStocksFromRepo.get().getStocks().size());
    }

}
