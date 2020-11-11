package com.keithcaff.stocksapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.dto.StockQueryDto;
import com.keithcaff.stocksapi.exception.StockRestClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class StockRestClient implements StockClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${stocks.client.apiKey}")
    String apiKey;
    @Value("${stocks.client.baseUrl}")
    String baseUrl;

    public StockRestClient(RestTemplateBuilder builder, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = builder.setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }

    public List<StockDto> searchStocks(String keywords) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("function", "SYMBOL_SEARCH")
                .queryParam("keywords", keywords)
                .queryParam("apikey", apiKey);
        return convertQueryToStockDtos(restTemplate.getForObject(builder.toUriString(), String.class));
    }

    private List<StockDto> convertQueryToStockDtos(String stockQueryJsonString) {
        try {
            JsonNode jsonNodeRoot = objectMapper.readTree(stockQueryJsonString);
            JsonNode bestMatches = jsonNodeRoot.get("bestMatches");
            List<StockQueryDto> stockQueryDtos = objectMapper.readValue(bestMatches.toString(), new TypeReference<List<StockQueryDto>>() {});
            return stockQueryDtos.stream().map(StockDto::new).collect(Collectors.toList());
        } catch (JsonProcessingException exception) {
            log.error(exception.getMessage());
            throw new StockRestClientException(exception.getMessage());
        }
    }
}
