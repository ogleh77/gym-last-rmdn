package com.example.gymproject.dto;

import com.example.gymproject.dto.main.UserService;
import com.example.gymproject.dto.services.CustomerService;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CustomException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class CustomerServiceTest {

    @Test
    void insertData() throws SQLException {
//        Users user = UserService.users().get(1);
//        Customers customer = CustomerService.fetchAllCustomer(user).get(0);
//        customer.setPhone("4303925");
//        Payments payment = new Payments();
//        payment.setCustomerFK(customer.getPhone());
//        payment.setDiscount(1);
//        payment.setAmountPaid(12);
//        payment.setPaidBy("Zaad");
//        payment.setExpDate(LocalDate.now().minusDays(60));
//        payment.setOnline(false);
//        payment.setBox(null);
//        payment.setMonth(LocalDate.now().getMonth().toString());
//        customer.getPayments().add(0, payment);
//        PaymentService.insertPayment(customer);
    }

    @Test
    void fetchAllCustomer() throws SQLException {
        Users user = UserService.users().get(0);

        System.out.println(user);
        System.out.println(CustomerService.fetchAllCustomer(user));
//        user.setRole("super_admin");
//        user.setGender("Female");
        //  UserService.update(user);
        //Customers customer = CustomerService.fetchAllCustomer(user).get(0);
        // customer.setGander("Female");
        //System.out.println(CustomerService.fetchAllCustomer(user));

        //CustomerService.insertOrUpdateCustomer(customer, false);
    }

    @Test
    void insertOrUpdateCustomer() {
    }

    @Test
    void fetchQualifiedOfflineCustomers() throws SQLException {
        Users user = UserService.users().get(0);

        System.out.println(CustomerService.fetchOfflineCustomer(user));
    }

    @Test
    void deleteCustomer() throws SQLException {
        Users user = UserService.users().get(0);
        CustomerService.deleteCustomer(CustomerService.fetchAllCustomer(user).get(0));
    }

    @Test
    void fetchOfflineCustomer() {
    }

    @Test
    void fetchOnlineCustomer() {
    }

    @Test
    void predictNextId() throws CustomException {
        System.out.println(CustomerService.predictNextId());
    }
}