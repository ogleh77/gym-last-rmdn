package com.example.gymproject;

import com.example.gymproject.dto.CustomerService;
import com.example.gymproject.dto.UserService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.validatecontrolles.DashboardController;
import com.example.gymproject.validatecontrolles.info.WarningController;
import com.example.gymproject.validatecontrolles.main.RegistrationController;
import com.example.gymproject.validatecontrolles.users.UpdateUserController;
import com.example.gymproject.validatecontrolles.users.UserCreateController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymproject/views/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        DashboardController controller=fxmlLoader.getController();
        controller.setActiveUser(UserService.users().get(0));
        ObservableList<Customers>warningList= FXCollections.observableArrayList();
        controller.setWarningList(warningList);
      //  controller.setUser(UserService.users().get(1));
        //WarningController controller=fxmlLoader.getController();
        //controller.se
//        RegistrationController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.users().get(0));
//        controller.setCustomer(CustomerService.fetchAllCustomer(UserService.users().get(0)).get(0));
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    // TODO: 23/03/2023 Insha Allah customer payment pending and un pend  
    // TODO: 13/03/2023 Trigger hadii customer phone ka la update gareyo paymentkana updategareya insha Allah
    public static void main(String[] args) {
        launch();
    }
}