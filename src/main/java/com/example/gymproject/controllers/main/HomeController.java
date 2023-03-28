package com.example.gymproject.controllers.main;

import com.example.gymproject.controllers.done.CustomerInfoController;
import com.example.gymproject.controllers.done.PaymentController;
import com.example.gymproject.controllers.main.RegistrationController;
import com.example.gymproject.dto.CustomerService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HomeController extends CommonClass implements Initializable {
    @FXML
    private TableColumn<Customers, Integer> customerId;

    @FXML
    private TableColumn<Customers, String> fullName;

    @FXML
    private TableColumn<Customers, String> gander;
    @FXML
    private TableColumn<Customers, String> phone;

    @FXML
    private TableColumn<Customers, String> shift;
    @FXML
    private TableView<Customers> tableView;

    @FXML
    private TableColumn<Customers, String> address;


    @FXML
    private TableColumn<Customers, String> imagePath;
    @FXML
    private TextField search;
    @FXML
    private TableColumn<Customers, Double> weight;

    private ObservableList<Customers> customersList;
    private FilteredList<Customers> filteredList;
    private SortedList<Customers> sortedList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println();
        Platform.runLater(() -> {
            initTable();
            searchFilter();
        });
        // TODO: 29/03/2023 madama aanu name ku update garobayn kala saar datada
    }

    private void initTable() {
        System.out.println("called init method in home");
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullName.setCellValueFactory(customers -> new SimpleStringProperty(customers.getValue().firstNameProperty().get() + "   " + customers.getValue().getMiddleName() + "   " + customers.getValue().getLastName()));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        gander.setCellValueFactory(new PropertyValueFactory<>("gander"));
        shift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        imagePath.setCellValueFactory(customers ->
                new SimpleStringProperty(customers.getValue().getImage() == null ? "------------"
                        : customers.getValue().getImage()));

        if (customersList.isEmpty()) {
            tableView.setPlaceholder(new Label("MACMIIL KUMA DIWAAN GASHANA"));
        } else {
            tableView.setItems(customersList);
        }
    }

    @FXML
    void paymentHandler() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            // FXMLLoader loader = openNormalWindow("/com/example/gymproject/views/customer-info.fxml", borderPane);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/payments.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage(StageStyle.UNDECORATED);
            PaymentController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void fullInfoHandler() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            // FXMLLoader loader = openNormalWindow("/com/example/gymproject/views/customer-info.fxml", borderPane);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/customer-info.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage(StageStyle.UNDECORATED);
            CustomerInfoController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void deleteHandler() {
//        if (tableView.getSelectionModel().getSelectedItem() != null) {
//
//            FXMLLoader loader = openNormalWindow("/com/example/gymdesktop2023/views/desing/customer-info.fxml", borderPane);
//            CustomerInfoController controller = loader.getController();
//            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
//            // controller.setBorderPane(borderPane);
//        }
        tableView.refresh();
    }

    @FXML
    void updateHandler() throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            // FXMLLoader loader = openNormalWindow("/com/example/gymproject/views/customer-info.fxml", borderPane);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/main/registrations.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage(StageStyle.UNDECORATED);
            RegistrationController controller = loader.getController();
            controller.setCustomer(tableView.getSelectionModel().getSelectedItem());
            controller.setActiveUser(activeUser);
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        try {
            customersList = CustomerService.fetchAllCustomer(activeUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }


    private void searchFilter() {
        filteredList = new FilteredList<>(customersList, b -> true);
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        search.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(customer -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            if (customer.getFirstName().contains(newValue.toLowerCase()) || customer.getFirstName().contains(newValue.toUpperCase())) {
                return true;
            } else if (customer.getPhone().contains(newValue)) {
                return true;
            } else if (customer.getLastName().contains(newValue.toLowerCase()) || customer.getLastName().contains(newValue.toUpperCase())) {
                return true;
            } else
                return customer.getMiddleName().contains(newValue.toLowerCase()) || customer.getMiddleName().contains(newValue.toUpperCase());
        }));

    }
}
