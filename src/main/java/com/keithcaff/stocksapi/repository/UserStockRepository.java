package com.keithcaff.stocksapi.repository;

import com.keithcaff.stocksapi.entity.UserStock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStockRepository extends MongoRepository<UserStock, String>, StockRepositoryCustomQueries {
    Optional<UserStock> findByUserId (String userId);
}

