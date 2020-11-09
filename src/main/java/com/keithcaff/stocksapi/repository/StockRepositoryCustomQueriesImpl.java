package com.keithcaff.stocksapi.repository;

import com.keithcaff.stocksapi.dto.StockDto;
import com.keithcaff.stocksapi.entity.Stock;
import com.keithcaff.stocksapi.entity.UserStock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class StockRepositoryCustomQueriesImpl implements StockRepositoryCustomQueries {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<UserStock> updateUserStocks(Set<Stock> updatedStocks, String userStockId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userStockId));
        Update update = new Update();
        update.set("stocks", updatedStocks);
        UserStock result = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), UserStock.class);
        return Optional.ofNullable(result);
    }
}
