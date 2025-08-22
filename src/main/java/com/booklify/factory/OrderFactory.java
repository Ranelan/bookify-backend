package com.booklify.factory;

import com.booklify.domain.Order;
import com.booklify.domain.RegularUser;
import com.booklify.util.Helper;

import java.time.LocalDateTime;

public class OrderFactory {

    public static Order createOrder(LocalDateTime orderDate, RegularUser regularUser){
        if (!Helper.isValidDateTime(orderDate)
                ||regularUser==null){
            return null;
        }

        return new Order.OrderBuilder()
                .setOrderDate(orderDate)
                .setRegularUser(regularUser)
                .build();
    }
}
