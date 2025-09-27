package com.booklify.factory;

import com.booklify.domain.Address;
import com.booklify.domain.Order;
import com.booklify.domain.RegularUser;

import java.util.List;

public class AddressFactory {
    public static Address createAddress(String street, String suburb, String city, String province, String country, String postalCode,RegularUser user, List<Order> orders){


        if(street.isEmpty() ){
            return null;
        }
        if(suburb == null||suburb.isEmpty() ){
            return null;
        }
        if(province== null||province.isEmpty() ){
            return null;
        }

        if(country == null||country.isEmpty() ){
            return null;
        }

        if(city == null||city.isEmpty() ){
            return null;
        }
        if (postalCode == null||postalCode.isEmpty()) {
            return null;
        }
         if (user == null) {
             return null;
        }
        if (orders == null) {
          return null;
        }
        return new Address.Builder()
                .setStreet(street)
                .setSuburb(suburb)
                .setCity(city)
                .setProvince(province)
                .setCountry(country)
                .setPostalCode(postalCode)
                .setUser(user)
                .setOrders(orders)
                .build();
    }


}

