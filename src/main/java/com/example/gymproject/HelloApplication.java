package com.example.gymproject;

import com.example.gymproject.controllers.done.UpdateUserController;
import com.example.gymproject.dto.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymproject/views/gym.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
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