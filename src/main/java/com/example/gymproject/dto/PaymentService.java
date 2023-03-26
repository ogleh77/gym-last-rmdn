package com.example.gymproject.dto;

import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Payments;
import com.example.gymproject.helpers.CustomException;
import com.example.gymproject.model.PaymentModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class PaymentService {
    private static final PaymentModel paymentModel = new PaymentModel();

    public static void insertPayment(Customers customer) throws SQLException {
        try {
            String customerGander = customer.getGander();
            paymentModel.insertPayment(customer.getPhone(), customerGander, customer.getPayments().get(0));
        } catch (SQLException e) {
            throw new CustomException("Khalad ayaaa ka dhacay " + e.getMessage() + " " + "\n fadlan dib u search garee customerka kadibna payment usamee");
        }

    }

    public static ObservableList<Payments> fetchAllCustomersPayments(String customerPhone) throws CustomException {
        try {
            return paymentModel.fetchAllCustomersPayments(customerPhone);
        } catch (SQLException e) {
            throw new CustomException("Khalad aya dhacay marka lasoo akhrinayay macmilkan payments kisa \n" +
                    e.getMessage());
        }

    }
}
