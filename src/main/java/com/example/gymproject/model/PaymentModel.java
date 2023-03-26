package com.example.gymproject.model;

import com.example.gymproject.dto.BoxService;
import com.example.gymproject.entity.Box;
import com.example.gymproject.entity.Payments;
import com.example.gymproject.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PaymentModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insertPayment(String customerPhone, String customerGender, Payments payment) throws SQLException {
        connection.setAutoCommit(false);

        try {
            String insertPaymentQuery = "INSERT INTO payments(exp_date, amount_paid, paid_by," + "discount,poxing,box_fk, customer_phone_fk,month) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertPaymentQuery);
            ps.setString(1, payment.getExpDate().toString());
            ps.setDouble(2, payment.getAmountPaid());
            ps.setString(3, payment.getPaidBy());
            ps.setDouble(4, payment.getDiscount());
            ps.setBoolean(5, payment.isPoxing());

            if (payment.getBox() == null) {
                ps.setString(6, null);
            } else {
                ps.setInt(6, payment.getBox().getBoxId());
                BoxService.updateBox(payment.getBox());
            }

            ps.setString(7, customerPhone);
            ps.setString(8, payment.getMonth());
            ps.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public ObservableList<Payments> fetchAllCustomersPayments(String phone) throws SQLException {
        //-------Fetch payments according to customer that belongs--------tested......

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        Payments payment = null;
        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + phone + " ORDER BY exp_date DESC");

        return getPayments(payments, statement, rs);
    }

    //__________------------Helpers____________------


    private ObservableList<Payments> getPayments(ObservableList<Payments> payments, Statement statement, ResultSet rs) throws SQLException {
        Payments payment;
        while (rs.next()) {

            Box box = null;
            if (rs.getString("box_fk") != null) {
                box = new Box(rs.getInt("box_id"), rs.getString("box_name"), rs.getBoolean("is_ready"));
            }
            payment = getPayments(rs);
            payment.setBox(box);
            payments.add(payment);

        }
        statement.close();
        rs.close();
        return payments;
    }

    private Payments getPayments(ResultSet rs) throws SQLException {
        Payments payment = new Payments(rs.getInt("payment_id"));
        payment.setPaymentDate(rs.getString("payment_date"));
        payment.setExpDate(LocalDate.parse(rs.getString("exp_date")));
        payment.setAmountPaid(rs.getDouble("amount_paid"));
        payment.setPaidBy(rs.getString("paid_by"));
        payment.setPoxing(rs.getBoolean("poxing"));
        payment.setDiscount(rs.getDouble("discount"));
        payment.setCustomerFK(rs.getString("customer_phone_fk"));
        payment.setOnline(rs.getBoolean("is_online"));
        payment.setYear(rs.getString("year"));
        payment.setPending(rs.getBoolean("pending"));
        payment.setMonth(rs.getString("month"));
        return payment;
    }

}
