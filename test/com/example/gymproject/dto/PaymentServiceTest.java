package com.example.gymproject.dto;

import com.example.gymproject.dto.services.PaymentService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class PaymentServiceTest {

    @Test
    void fetchAllCustomersPayments() throws SQLException {
        System.out.println(PaymentService.fetchAllCustomersPayments("4303923"));
    }
}