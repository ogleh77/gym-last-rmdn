package com.example.gymproject.validatecontrolles.info;


import animatefx.animation.FadeOut;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.validatecontrolles.service.CardController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class WarningController extends CommonClass implements Initializable {
    @FXML
    private VBox vbox;
    private Stage thisStage;
    @FXML
    private AnchorPane warningPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> this.thisStage = (Stage) warningPane.getScene().getWindow());
    }

    @FXML
    void cancelHandler() {
        FadeOut fadeOut = new FadeOut(warningPane);
        fadeOut.setOnFinished(e -> {
            thisStage.close();
        });
        fadeOut.setSpeed(2);
        fadeOut.play();
    }

    public void setOutdatedCustomers(ObservableList<Customers> outdatedCustomers) {
        if (!outdatedCustomers.isEmpty()) {
            FXMLLoader loader;
            AnchorPane anchorPane;
            for (Customers customer : outdatedCustomers) {
                loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/info/customer-card.fxml"));
                try {
                    anchorPane = loader.load();
                    CardController controller = loader.getController();
                    controller.setCustomer(customer);

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                vbox.getChildren().add(anchorPane);
            }
        } else {
            Label label = new Label("Ma jirto macamiil wakhtigoodu dhamaad ku dhowyahay.");
            label.setStyle("-fx-font-size: 15;-fx-font-family: Verdana");
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(label);
            vbox.setPrefHeight(300);

        }

    }

}
