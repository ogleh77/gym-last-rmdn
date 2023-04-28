package com.example.gymproject.validatecontrolles.main;

import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.validatecontrolles.info.OutDatedController;
import com.example.gymproject.validatecontrolles.main.HomeController;
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
    void loginMenuHandler() {
        System.out.println("Add user");
    }

    @FXML
    void homeMenuHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/main/home.fxml", borderPane,
                sidePane, menuHBo, notificationsHBox);
        HomeController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }

    @FXML
    void outDatedMenuHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/info/outdated.fxml", borderPane,
                sidePane, menuHBo, notificationsHBox);
        OutDatedController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    @FXML
    void reportMenuHandler() throws IOException {
        openWindow("/com/example/gymproject/views/info/dailyReports.fxml", borderPane, sidePane,
                menuHBo, notificationsHBox);
    }


    @FXML
    void registrationMenuHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/main/registrations.fxml", borderPane, sidePane, menuHBo, notificationsHBox);
        RegistrationController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
        controller.setCurrentGym(currentGym);
        System.out.println("Reg user");
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
