package com.keithcaff.stocksapi.unit;

import com.keithcaff.stocksapi.client.StockRestClient;
import com.keithcaff.stocksapi.dto.StockDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@RestClientTest(StockRestClient.class)
public class StockClientTest {

    @Autowired
    StockRestClient stockRestClient;

    @Autowired
    private MockRestServiceServer server;

    @Value("${stocks.client.apiKey}")
    String apiKey;

    @Value("${stocks.client.baseUrl}")
    String baseUrl;

    @BeforeEach
    public void setUpServer() throws Exception {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/successfulExternalSearch.json")));

        this.server.expect(requestTo(matchesPattern(String.format("%s.*", baseUrl))))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("function", "SYMBOL_SEARCH"))
                .andExpect(queryParam("keywords", "apple"))
                .andExpect(queryParam("apikey", apiKey))
                .andRespond(withSuccess(content, MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should call external search stock api with expected params and convert response to StockDtos")
    public void shouldMakeRequestToExternalStockSearchApi() {
        // given
        StockDto expectedFirstItem = new StockDto("AAPL", "Apple Inc.", "United States", "USD");
        List<StockDto> searchResults = this.stockRestClient.searchStocks("apple");
        // then
        assertFalse(searchResults.isEmpty());
        assertEquals(7, searchResults.size());
        assertEquals(expectedFirstItem, searchResults.get(0));
    }

}
