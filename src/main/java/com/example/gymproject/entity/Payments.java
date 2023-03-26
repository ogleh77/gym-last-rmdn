package com.example.gymproject.entity;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Payments {
    private final int paymentID;
    private final SimpleStringProperty paymentDate = new SimpleStringProperty();
    private LocalDate expDate;
    private final SimpleStringProperty month = new SimpleStringProperty();
    private final SimpleStringProperty year = new SimpleStringProperty();
    private final SimpleDoubleProperty amountPaid = new SimpleDoubleProperty();
    private final SimpleStringProperty paidBy = new SimpleStringProperty();
    private final SimpleDoubleProperty discount = new SimpleDoubleProperty();
    private final SimpleBooleanProperty poxing = new SimpleBooleanProperty();
    private Box box;
    private String customerFK;
    private SimpleBooleanProperty online = new SimpleBooleanProperty();
    private SimpleBooleanProperty pending = new SimpleBooleanProperty();

    public Payments() {
        this.paymentID = 0;
    }

    public Payments(int paymentID) {
        this.paymentID = paymentID;
    }

    public Payments(int paymentID, String paymentDate, LocalDate expDate, String month, String year, double amountPaid, String paidBy, double discount, boolean poxing, String customerFK, boolean online, boolean pending) {
        this.paymentID = paymentID;
        this.setPaymentDate(paymentDate);
        this.expDate = expDate;
        this.setMonth(month);
        this.setYear(year);
        this.setAmountPaid(amountPaid);
        this.setPaidBy(paidBy);
        this.setDiscount(discount);
        this.setPoxing(poxing);
        this.customerFK = customerFK;
        this.setOnline(online);
        this.setPending(pending);
    }

    public int getPaymentID() {
        return paymentID;
    }

    public String getPaymentDate() {
        return paymentDate.get();
    }

    public SimpleStringProperty paymentDateProperty() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate.set(paymentDate);
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public String getMonth() {
        return month.get();
    }

    public SimpleStringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public String getYear() {
        return year.get();
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public double getAmountPaid() {
        return amountPaid.get();
    }

    public SimpleDoubleProperty amountPaidProperty() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid.set(amountPaid);
    }

    public String getPaidBy() {
        return paidBy.get();
    }

    public SimpleStringProperty paidByProperty() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy.set(paidBy);
    }

    public double getDiscount() {
        return discount.get();
    }

    public SimpleDoubleProperty discountProperty() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public boolean isPoxing() {
        return poxing.get();
    }

    public SimpleBooleanProperty poxingProperty() {
        return poxing;
    }

    public void setPoxing(boolean poxing) {
        this.poxing.set(poxing);
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public String getCustomerFK() {
        return customerFK;
    }

    public boolean isOnline() {
        return online.get();
    }

    public SimpleBooleanProperty onlineProperty() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online.set(online);
    }

    public boolean isPending() {
        return pending.get();
    }

    public SimpleBooleanProperty pendingProperty() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending.set(pending);
    }

    public void setCustomerFK(String customerFK) {
        this.customerFK = customerFK;
    }

    @Override
    public String toString() {
        return "Payments{" + "paymentID=" + paymentID + ", paymentDate='" + paymentDate + '\'' + ", expDate=" + expDate + ", month='" + month + '\'' + ", year='" + year + '\'' + ", amountPaid=" + amountPaid + ", paidBy='" + paidBy + '\'' + ", discount=" + discount + ", poxing=" + poxing + ", box=" + box + ", customerFK='" + customerFK + '\'' + ", online=" + online + ", pending=" + pending + '}';
    }
}