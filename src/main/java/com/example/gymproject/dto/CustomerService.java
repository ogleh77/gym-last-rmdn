package com.example.gymproject.dto;

import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CustomException;
import com.example.gymproject.model.CustomerModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerService {
    private static final CustomerModel customerModel = new CustomerModel();
    private static ObservableList<Customers> allCustomersList;
    private static ObservableList<Customers> offlineCustomers;
    private static ObservableList<Customers> onlineCustomers;

    public static void insertOrUpdateCustomer(Customers customer, boolean newCustomer) throws SQLException {
        try {
            if (newCustomer) {
                insertCustomer(customer);
            } else {
                updateCustomer(customer);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: customers.phone)")) {
                throw new CustomException("Lanbarka " + customer.getPhone() + " hore ayaa loo diwaan geshay fadlan dooro lanbarkale");
            } else {
                throw new CustomException("Khalad ayaaa ka dhacay " + e.getMessage());
            }
        }
    }

    private static void insertCustomer(Customers customer) throws SQLException {
        customerModel.insert(customer);
    }

    private static void updateCustomer(Customers customer) throws SQLException {
        customerModel.update(customer);
    }

    public static ObservableList<Customers> fetchAllCustomer(Users activeUser) throws SQLException {
        if (allCustomersList == null) {
            allCustomersList = customerModel.fetchAllCustomers(activeUser);
        }
        System.out.println("Some one called me and I returned " + allCustomersList.hashCode());
        return allCustomersList;
    }

    public static ObservableList<Customers> fetchQualifiedOfflineCustomers(String customerQuery, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String from = fromDate.toString();
        String to = toDate.toString();
        ObservableList<Customers> offlineCustomers = customerModel.fetchQualifiedOfflineCustomers(customerQuery, from, to);
        //Collections.sort(offlineCustomers);
        // TODO: 26/03/2023 sort insha Allah
        System.out.println("I Service \n");
        System.out.println(offlineCustomers);
        return offlineCustomers;
    }


}
