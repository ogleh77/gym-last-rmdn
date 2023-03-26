package com.example.gymproject.dto;

import com.example.gymproject.entity.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class CustomerServiceTest {

    @Test
    void fetchAllCustomer() throws SQLException {

        Users user = UserService.users().get(1);
        user.setRole("super_admin");
        user.setGender("Female");
        //  UserService.update(user);
        //Customers customer = CustomerService.fetchAllCustomer(user).get(0);
        // customer.setGander("Female");
        System.out.println(CustomerService.fetchAllCustomer(user));

        //CustomerService.insertOrUpdateCustomer(customer, false);
    }
}