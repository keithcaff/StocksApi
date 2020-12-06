package com.keithcaff.stocksapi.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keithcaff.stocksapi.controller.StockController;
import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.dto.UserStockDto;
import com.keithcaff.stocksapi.entity.UserStock;
import com.keithcaff.stocksapi.exception.StockConflictException;
import com.keithcaff.stocksapi.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StockController.class)
public class StockControllerTest {
    private static final String USER_STOCKS_ENDPOINT = "/user/stocks";
    private static final String PUT_USER_STOCKS_ENDPOINT = "/user/stocks/{userStockId}";
    private static final String SEARCH_STOCKS_ENDPOINT = "/stocks/search";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StockService stockService;

    @WithAnonymousUser
    @Test
    @DisplayName("POST /user/stocks should redirect to login page")
    public void testPostNewUserStockAuthenticates() throws Exception {
        // given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(nikeStockDto));
        UserStock userStock = new UserStock(stockDtos);
        when(stockService.createUserStock(stockDtos)).thenReturn(userStock);
        String oktaLogin = "http://localhost/oauth2/authorization/okta";

        // when/then
        mockMvc.perform(post(USER_STOCKS_ENDPOINT)
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(asJsonString(stockDtos)))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, oktaLogin))
                .andDo(print());
    }

    @WithMockUser
    @Test
    @DisplayName("POST /user/stocks should create new user stocks")
    public void testPostNewUserStock() throws Exception {
        // given
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(new StockDto("NKE", "NIKE Inc.", "United States", "USD")));
        UserStock userStock = new UserStock(stockDtos);
        userStock.setId("1234");
        userStock.setUserId("user");
        when(stockService.createUserStock(stockDtos)).thenReturn(userStock);

        // when/then
        UserStockDto expectedResponse = new UserStockDto(userStock);
        mockMvc.perform(post(USER_STOCKS_ENDPOINT)
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(asJsonString(stockDtos)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(asJsonString(expectedResponse)));
    }

    @WithMockUser
    @Test
    @DisplayName("POST /user/stocks should reject invalid StockDTO")
    public void testPostInvalidUserStock() throws Exception {
        // given
        StockDto invalidDto = new StockDto("", null, "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(invalidDto));

        // when/then
        mockMvc.perform(post(USER_STOCKS_ENDPOINT)
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(asJsonString(stockDtos)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(("name: must not be blank"))))
                .andExpect(jsonPath("$.message", containsString(("symbol: must not be blank"))))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    @DisplayName("POST /user/stocks should return 403 response when UserStock resource already exists")
    public void testPostUserStockHandlesStockConflictException() throws Exception {
        // given
        StockDto nikeStockDto = new StockDto("NKE", "NIKE Inc.", "United States", "USD");
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(nikeStockDto));
        UserStock userStock = new UserStock(stockDtos);
        when(stockService.createUserStock(stockDtos)).thenThrow(new StockConflictException("User Stock already exists"));

        // when/then
        mockMvc.perform(post(USER_STOCKS_ENDPOINT)
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(asJsonString(stockDtos)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", containsString(("User Stock already exists"))));
    }

    @WithMockUser
    @Test
    @DisplayName("GET /stocks successfully search stocks")
    public void testSearchStocks() throws Exception {
        // given
        String keywordsParamValue = "apple";
        List<StockDto> stockDtos = Arrays.asList(
                new StockDto("AAPL", "Apple Inc.", "United States", "USD"),
                new StockDto("APLE", "Apple Hospitality REIT Inc.", "United States", "USD"));
        when(stockService.search(keywordsParamValue)).thenReturn(stockDtos);

        // when/then
        mockMvc.perform(get(SEARCH_STOCKS_ENDPOINT).param("keywords", keywordsParamValue))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(asJsonString(stockDtos)));
    }

    @WithMockUser
    @Test
    @DisplayName("GET /stocks should validate keywords param is not empty")
    public void testSearchStocksEmptyKeywords() throws Exception {
        // when/then
        mockMvc.perform(get(SEARCH_STOCKS_ENDPOINT).param("keywords", "")
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(("keywords: must not be blank"))))
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @WithMockUser
    @Test
    @DisplayName("GET /user/stocks should return all user's followed stocks")
    public void testGetUserStocks() throws Exception {
        // given
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(
                new StockDto("NKE", "NIKE Inc.", "United States", "USD"),
                new StockDto("AAPL", "Apple Inc.", "United States", "USD")));
        UserStock userStock = new UserStock(stockDtos);
        userStock.setId("1234");
        userStock.setUserId("user");
        when(stockService.getUserStocks("user")).thenReturn(Optional.of(userStock));

        // when/then
        UserStockDto expectedResponse = new UserStockDto(userStock);
        mockMvc.perform(get(USER_STOCKS_ENDPOINT)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(asJsonString(expectedResponse)));
    }

    @WithMockUser
    @Test
    @DisplayName("GET /user/stocks should return NOT FOUND response when user doesn't follow any stocks")
    public void testGetUserStocksNotFound() throws Exception {
        // given
        when(stockService.getUserStocks("user")).thenReturn(Optional.empty());

        // when/then
        mockMvc.perform(get(USER_STOCKS_ENDPOINT)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().reason(is("User does not follow any stocks")))
                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    @DisplayName("PUT /user/stocks/{userStockId} should update users currently followed stocks")
    public void testUpdateUserStocks() throws Exception {
        // given
        String userStocksId = "1234";
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(
                new StockDto("NKE", "NIKE Inc.", "United States", "USD"),
                new StockDto("AAPL", "Apple Inc.", "United States", "USD")));
        UserStock userStock = new UserStock(stockDtos);
        userStock.setId(userStocksId);
        userStock.setUserId("user");
        when(stockService.updateUserStocks(stockDtos, userStocksId)).thenReturn(Optional.of(userStock));

        // when/then
        UserStockDto expectedResponse = new UserStockDto(userStock);
        mockMvc.perform(put(PUT_USER_STOCKS_ENDPOINT, userStocksId)
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(asJsonString(stockDtos)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(asJsonString(expectedResponse)));
    }

    @WithMockUser
    @Test
    @DisplayName("PUT /user/stocks/{userStockId} should return NOT_FOUND response when non-existing userStockId passed")
    public void testUpdateUserStocksWithNoExistingStockId() throws Exception {
        // given
        String invalidUserStockId = "someInvalidUserStcokId";
        Set<StockDto> stockDtos = new HashSet<>(Arrays.asList(
                new StockDto("AAPL", "Apple Inc.", "United States", "USD")));
        when(stockService.updateUserStocks(stockDtos, invalidUserStockId)).thenReturn(Optional.empty());

        // when/then
        mockMvc.perform(put(PUT_USER_STOCKS_ENDPOINT, invalidUserStockId)
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(asJsonString(stockDtos)))
                .andExpect(status().isNotFound());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
