package com.example.gymproject.model;

import com.example.gymproject.dto.CustomerService;
import com.example.gymproject.dto.PaymentService;
import com.example.gymproject.dto.UserService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class CustomerModelTest {
    @Test
    void insertCustomer() throws SQLException {
        Users user = UserService.users().get(1);
        Customers customer = new Customers();
        customer.setPhone("4303924");
        customer.setGander("Female");
        customer.setWeight(100);
        customer.setImage(null);
        customer.setAddress("Hargeisa");
        customer.setWhoAdded(user.getUsername());
        customer.setMiddleName("Maxamed");
        customer.setFirstName("Saleban");
        customer.setLastName("Faadumo");
        customer.setShift("Morning");

        CustomerService.insertOrUpdateCustomer(customer, true);
    }

    @Test
    void fetchQualifiedOfflineCustomers() throws SQLException {

        CustomerService.fetchQualifiedOfflineCustomers("SELECT * FROM customers WHERE gander='Male'",
                LocalDate.now().minusDays(100), LocalDate.now());

//
//        System.out.println(PaymentService.fetchQualifiedOfflinePayment("4303925",
//                LocalDate.now().minusDays(100).toString(), LocalDate.now().toString()));

        System.out.println(LocalDate.now().minusDays(100));
    }
}