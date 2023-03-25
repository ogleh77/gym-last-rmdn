package com.example.gymproject.controllers;

import com.example.gymproject.controllers.done.UpdateUserController;
import com.example.gymproject.dto.GymService;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
    private Stage dashboardStage;
    private final Gym currentGym;

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

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }
// TODO: 25/03/2023 Hadii customer ku dashboard ku dhufto dhaman xidh top pane and side pane Insha Allah
}
