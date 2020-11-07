package com.keithcaff.stocksapi.repository;

import com.keithcaff.stocksapi.entity.UserStock;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;

@RepositoryEventHandler(UserStock.class)
public class UserStockEventHandler {
    @HandleBeforeSave
    public void handleStockSave(UserStock stock) {
        String user =  SecurityContextHolder.getContext().getAuthentication().getName();
        stock.setUserId(user);
    }
}
