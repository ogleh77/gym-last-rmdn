package com.example.gymproject.validatecontrolles;

import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.validatecontrolles.main.RegistrationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardMenuController extends CommonClass implements Initializable {
    private BorderPane borderPane;
    private VBox sidePane;
    private HBox menuHBo;
    private StackPane notificationsHBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void registrationHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/main/registrations.fxml", borderPane,
                sidePane, menuHBo, notificationsHBox);
        RegistrationController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
        controller.setCurrentGym(currentGym);
    }

    @FXML
    void reportHandler() {

    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }

    public void setMenus(BorderPane borderPane, VBox sidePane, HBox menuHBox, StackPane notificationsHBox) {
        this.borderPane = borderPane;
        this.sidePane = sidePane;
        this.notificationsHBox = notificationsHBox;
        this.menuHBo = menuHBox;
    }
}
