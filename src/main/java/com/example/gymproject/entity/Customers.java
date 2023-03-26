package com.example.gymproject.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customers {
    private int customerId;
    private final SimpleStringProperty firstName = new SimpleStringProperty();
    private final SimpleStringProperty lastName = new SimpleStringProperty();
    private final SimpleStringProperty middleName = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();
    private final SimpleStringProperty gander = new SimpleStringProperty();
    private final SimpleStringProperty shift = new SimpleStringProperty();
    private final SimpleStringProperty address = new SimpleStringProperty();
    private final SimpleStringProperty image = new SimpleStringProperty();
    private final SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private final SimpleStringProperty whoAdded = new SimpleStringProperty();

    private ObservableList<Payments> payments;
    public Customers(int customerId, String firstName, String lastName, String middleName, String phone, String gander, String shift, String address, String image, double weight, String whoAdded) {
        this.customerId = customerId;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setMiddleName(middleName);
        this.setPhone(phone);
        this.setGander(gander);
        this.setShift(shift);
        this.setAddress(address);
        this.setImage(image);
        this.setWeight(weight);
        this.setWhoAdded(whoAdded);
        this.payments = FXCollections.observableArrayList();
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public SimpleStringProperty middleNameProperty() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getGander() {
        return gander.get();
    }

    public SimpleStringProperty ganderProperty() {
        return gander;
    }

    public void setGander(String gander) {
        this.gander.set(gander);
    }

    public String getShift() {
        return shift.get();
    }

    public SimpleStringProperty shiftProperty() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift.set(shift);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getImage() {
        return image.get();
    }

    public SimpleStringProperty imageProperty() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }

    public double getWeight() {
        return weight.get();
    }

    public SimpleDoubleProperty weightProperty() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public String getWhoAdded() {
        return whoAdded.get();
    }

    public SimpleStringProperty whoAddedProperty() {
        return whoAdded;
    }

    public void setWhoAdded(String whoAdded) {
        this.whoAdded.set(whoAdded);
    }

    public ObservableList<Payments> getPayments() {
        return payments;
    }

    public void setPayments(ObservableList<Payments> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "\n [customerId: " +
                customerId + " firstname: " +
                firstName + "  lastname: " +
                lastName + " gander " + gander + " phone: " + phone + "weight: " + weight + "\n payments: " + null + "]\n\n";
    }
}
