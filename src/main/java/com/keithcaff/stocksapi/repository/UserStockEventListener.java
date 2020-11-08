package com.keithcaff.stocksapi.repository;

import com.keithcaff.stocksapi.entity.UserStock;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserStockEventListener extends AbstractMongoEventListener<UserStock> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<UserStock> event) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        event.getSource().setUserId(user);
        super.onBeforeConvert(event);
    }

}
