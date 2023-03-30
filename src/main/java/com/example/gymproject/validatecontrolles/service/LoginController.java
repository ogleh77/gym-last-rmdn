package com.example.gymproject.validatecontrolles.service;

import animatefx.animation.FadeOut;
import com.example.gymproject.controllers.service.SplashScreenController;
import com.example.gymproject.dto.UserService;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends CommonClass implements Initializable {
    public AnchorPane loginPane;
    @FXML
    private JFXButton loginBtn;

    @FXML
    private PasswordField password;


    private ObservableList<Users> users;
    private Stage currentStage;

    @FXML
    private ComboBox<Users> userCombo;


    public LoginController() {
        try {
            users = UserService.users();
        } catch (SQLException e) {
            errorMessage("Khalad ba ka dhacay " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            currentStage = (Stage) userCombo.getScene().getWindow();
            userCombo.setItems(users);
            getMandatoryFields().addAll(userCombo, password);
        });

        service.setOnSucceeded(e -> {
            loginBtn.setGraphic(null);

            if (error) {
                errorMessage("Fadlan hubi username ka ama passwordka aad gelisay");
            } else {
                closeLogin();

                System.out.println("Loged");
                try {
                    openSplash();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @FXML
    void loginHandler() {
        if (isValid(getMandatoryFields(), null)) {
            loginBtn.setContentDisplay(ContentDisplay.RIGHT);
            if (start) {
                service.restart();
                loginBtn.setGraphic(getLoadingImageView());
            } else {
                service.start();
                loginBtn.setGraphic(getLoadingImageView());
                start = true;
            }
        }
    }

    @FXML
    void exitHandler() {
        closeLogin();
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    Users activeUser = userCombo.getValue();
                    error = !password.getText().equals(activeUser.getPassword());
                    return null;
                }
            };
        }
    };

    private void openSplash() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/service/splash-screen.fxml"));
        Scene scene = new Scene(loader.load());
        SplashScreenController controller = loader.getController();
        controller.setActiveUser(userCombo.getValue());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private void closeLogin() {
        FadeOut fadeOut = new FadeOut(loginPane);
        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            currentStage.close();
        });
    }
}
