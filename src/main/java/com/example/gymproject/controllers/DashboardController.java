package com.example.gymproject.controllers;

import animatefx.animation.FadeIn;
import com.example.gymproject.controllers.done.UpdateUserController;
import com.example.gymproject.controllers.service.WarningController;
import com.example.gymproject.dto.GymService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label gymName;
    @FXML
    private Label warningLabel;
    @FXML
    private HBox warningParent;
    private Stage dashboardStage;
    private final Gym currentGym;
    private ObservableList<Customers> warningList;

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            dashboardStage = (Stage) borderPane.getScene().getWindow();
            gymName.textProperty().bind(currentGym.gymNameProperty());
        });
    }


    //----------------_____Setting handler_____-------------
    @FXML
    void profileHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/user-update.fxml"));
        Scene scene = new Scene(loader.load());
        UpdateUserController controller = loader.getController();
        controller.setActiveUser(activeUser);
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gymHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/gym.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void userCreationHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/user-creation.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void updateUserHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/user-chooser.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void warningHandler(MouseEvent event) throws IOException {

        if (!warningList.isEmpty() && event.getClickCount() == 1) {
            warningParent.setVisible(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/service/warning.fxml"));
            Scene scene = new Scene(loader.load());
            WarningController controller = loader.getController();
            controller.setOutdatedCustomers(warningList);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.showAndWait();
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }

    public void setWarningList(ObservableList<Customers> warningList) {
        this.warningList = warningList;
        if (warningList.isEmpty()) {
            warningParent.setVisible(false);
        } else {
            warningLabel.setText(warningList.size() < 9 ? String.valueOf(warningList.size()) : "9 +");
            FadeIn fadeIn = new FadeIn(warningParent);
            fadeIn.setCycleCount(20);
            fadeIn.play();
        }
    }
    // TODO: 25/03/2023 Hadii customer ku dashboard ku dhufto dhaman xidh top pane and side pane Insha Allah
}
