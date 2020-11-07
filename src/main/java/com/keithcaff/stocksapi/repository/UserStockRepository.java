package com.keithcaff.stocksapi.repository;
import com.keithcaff.stocksapi.entity.UserStock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStockRepository extends MongoRepository<UserStock, String> {

}

