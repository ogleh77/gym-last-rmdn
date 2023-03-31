package com.example.gymproject.validatecontrolles;

import animatefx.animation.FadeIn;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import com.example.gymproject.dto.GymService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.validatecontrolles.info.OutDatedController;
import com.example.gymproject.validatecontrolles.info.WarningController;
import com.example.gymproject.validatecontrolles.main.HomeController;
import com.example.gymproject.validatecontrolles.main.RegistrationController;
import com.example.gymproject.validatecontrolles.users.UpdateUserController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    @FXML
    private VBox sidePane;
    @FXML
    private StackPane warningStack;
    private Stage dashboardStage;
    private final Gym currentGym;
    private ObservableList<Customers> warningList;
    private boolean visible = false;

    @FXML
    private HBox menuHBox;
    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            dashboardStage = (Stage) borderPane.getScene().getWindow();
            gymName.textProperty().bind(currentGym.gymNameProperty());
            borderPane.setLeft(null);
            try {
                dashboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void menuClicked() {
        if (visible) {
            SlideOutLeft slideOutLeft = new SlideOutLeft();
            slideOutLeft.setNode(sidePane);
            slideOutLeft.play();
            slideOutLeft.setOnFinished(e -> borderPane.setLeft(null));
        } else {
            new SlideInLeft(sidePane).play();
            borderPane.setLeft(sidePane);
        }
        visible = !visible;
    }

    //----------------_____Setting handler_____-------------
    @FXML
    void homeHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/main/home.fxml", borderPane,
                sidePane, null, warningStack);
        HomeController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }

    @FXML
    void registrationHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/main/registrations.fxml", borderPane,
                sidePane, null, warningStack);
        RegistrationController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
        controller.setCurrentGym(currentGym);


        // TODO: 30/03/2023 Insha Allah samee dashboar pane gooniya 
    }

    @FXML
    void outdatedHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymproject/views/info/outdated.fxml", borderPane,
                sidePane, null, warningStack);
        OutDatedController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    @FXML
    void reportHandler() throws IOException {
        System.out.println("clicked");
        openWindow("/com/example/gymproject/views/info/dailyReports.fxml", borderPane,
                sidePane, null, warningStack);

    }


    @FXML
    void profileHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/users/user-update.fxml"));
        Scene scene = new Scene(loader.load());
        UpdateUserController controller = loader.getController();
        controller.setActiveUser(activeUser);
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gymHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/service/gym.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void userCreationHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/users/user-creation.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void updateUserHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/users/user-chooser.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void warningHandler(MouseEvent event) throws IOException {
        if (!warningList.isEmpty()) {
            warningParent.setVisible(false);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/info/warning.fxml"));
        Scene scene = new Scene(loader.load());
        WarningController controller = loader.getController();
        controller.setOutdatedCustomers(warningList);
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    void backupHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/service/backup.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();
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

    @FXML
    void dashboardHandler() throws IOException {
        dashboard();
        borderPane.setLeft(null);
        menuHBox.setVisible(!menuHBox.isVisible());
    }

    private void dashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/main/dashboard-menu.fxml"));
        AnchorPane anchorPane = loader.load();
        DashboardMenuController controller = loader.getController();
        controller.setMenus(borderPane, sidePane,menuHBox, warningStack);
        controller.setActiveUser(activeUser);
        borderPane.setCenter(anchorPane);
    }
    // TODO: 25/03/2023 Hadii customer ku dashboard ku dhufto dhaman xidh top pane and side pane Insha Allah
}
