package com.example.gymproject.model.main;

import com.example.gymproject.dto.main.BoxService;
import com.example.gymproject.entity.Box;
import com.example.gymproject.entity.Payments;
import com.example.gymproject.helpers.CustomException;
import com.example.gymproject.helpers.DbConnection;
import com.example.gymproject.model.services.DailyReportModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

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
            makeReport(payment, customerGender);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void holdPayment(Payments payment, int daysRemain) throws SQLException {
        connection.setAutoCommit(false);

        String holdQuery = "INSERT INTO pending(days_remain,payment_fk)" + "VALUES (" + daysRemain + "," + payment.getPaymentID() + ")";

        try (Statement statement = connection.createStatement()) {
            String paymentQuery = "UPDATE payments SET is_online=false,pending=true WHERE payment_id=" + payment.getPaymentID();
            if (payment.getBox() != null) {
                System.out.println("Payment has a box");
                paymentQuery = "UPDATE payments SET is_online=false,pending=true,box_fk=null WHERE payment_id=" + payment.getPaymentID();
                BoxService.updateBox(payment.getBox());
            } else {
                System.out.println("Payment dosnt have a box");
            }

            statement.addBatch(holdQuery);
            statement.addBatch(paymentQuery);
            statement.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public void unHold(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        try {

            int daysRemain = daysRemain(payment.getPaymentID());

            if (daysRemain != 0) {

                LocalDate remainedDays = LocalDate.now().plusDays(daysRemain);

                String deleteQuery = "DELETE FROM pending WHERE payment_fk=" + payment.getPaymentID();

                String unPendPayment = "UPDATE payments SET is_online=true, pending=false," + "exp_date='" + remainedDays + "' WHERE payment_id=" + payment.getPaymentID();

                statement.addBatch(deleteQuery);
                statement.addBatch(unPendPayment);
                statement.executeBatch();

                connection.commit();
                payment.setExpDate(LocalDate.now().plusDays(daysRemain));
            } else {
                throw new CustomException("Paymentkan lama xayarin fadlan iska hubi");
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }


    }

    public ObservableList<Payments> fetchCustomersOnlinePayment(String customerPhone) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        Payments payment = null;
        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + customerPhone + "  AND pending=false AND is_online=true ORDER BY exp_date DESC ");

        return getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchCustomersOfflinePayment(String customerPhone) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + customerPhone + "  AND pending=false AND is_online=false ");


        return getPayments(payments, statement, rs);

    }

    public ObservableList<Payments> fetchAllCustomersPayments(String phone) throws SQLException {
        //-------Fetch payments according to customer that belongs--------tested......

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        Payments payment = null;
        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + phone + " ORDER BY exp_date ASC ");

        return getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchQualifiedOfflinePayment(String customerPhone, String fromDate, String toDate) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk='" + customerPhone + "'" + " AND is_online=false AND pending=false " + "AND exp_date between '" + fromDate + "' AND '" + toDate + "';";

        ResultSet rs = statement.executeQuery(query);

        getPayments(payments, statement, rs);
        statement.close();
        rs.close();
        return payments;

    }
    //__________------------Helpers____________-----------

    private static void makeReport(Payments payment, String customerGender) throws SQLException {
        Statement st = connection.createStatement();
        if (customerGender.equals("Male") && payment.getBox() != null) {
            DailyReportModel.dailyReportMaleWithBox(st);
        } else if (customerGender.equals("Female") && payment.getBox() != null) {
            DailyReportModel.dailyReportFemaleWithBox(st);
        } else if (payment.getBox() == null && customerGender.equals("Male")) {
            DailyReportModel.dailyReportMaleWithOutBox(st);
        } else if (payment.getBox() == null && customerGender.equals("Female")) {
            DailyReportModel.dailyReportFemaleWithOutBox(st);
        }
        int arr[] = st.executeBatch();
        System.out.println(Arrays.toString(arr));
        st.close();
    }

    private int daysRemain(int paymentID) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM pending WHERE payment_fk=" + paymentID);
        if (rs.next()) {
            int daysRemain = rs.getInt("days_remain");
            System.out.println("Founded");
            return daysRemain;
        } else {
            System.out.println("Not exist");
        }
        statement.close();
        rs.close();
        return 0;
    }

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
        return new Payments(rs.getInt("payment_id"), rs.getString("payment_date"), LocalDate.parse(rs.getString("exp_date")), rs.getString("month"), rs.getString("year"), rs.getDouble("amount_paid"), rs.getString("paid_by"), rs.getDouble("discount"), rs.getBoolean("poxing"), rs.getString("customer_phone_fk"), rs.getBoolean("is_online"), rs.getBoolean("pending"));
    }

}
