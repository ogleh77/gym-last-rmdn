package com.example.gymproject.validatecontrolles;

import animatefx.animation.*;
import com.example.gymproject.dto.main.GymService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.validatecontrolles.info.OutDatedController;
import com.example.gymproject.validatecontrolles.info.WarningController;
import com.example.gymproject.validatecontrolles.main.DashboardMenuController;
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
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
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
    private final Gym currentGym;
    private ObservableList<Customers> warningList;
    private boolean visible = false;

    @FXML
    private HBox menuHBox;

    @FXML
    private Circle activeProfile;
    @FXML
    private Label activeUserName;
    @FXML
    private MenuItem addUserBtn;
    @FXML
    private MenuItem backupBtn;
    @FXML
    private MenuItem updateUserBtn;
    @FXML
    private MenuItem gymBtn;

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
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
    void warningHandler() throws IOException {
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
        activeUserName.setText(activeUser.getUsername() + " [" + activeUser.getRole() + "]");
        URL url;
        final String[] profileImages = {
                "/com/example/gymproject/style/icons/man-profile.jpeg",
                "/com/example/gymproject/style/icons/woman-hijap.jpeg"
        };

        if (activeUser.getGender().equals("Male")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[0]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(activeUser.getImage());
                Image image = new Image(bis);
                activeProfile.setFill(new ImagePattern(image));
            }

        } else if (activeUser.getGender().equals("Female")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[1]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(activeUser.getImage());
                Image image = new Image(bis);
                activeProfile.setFill(new ImagePattern(image));
            }
        }

        if (!activeUser.getRole().equals("super_admin")) {
            backupBtn.setDisable(true);
            updateUserBtn.setDisable(true);
            addUserBtn.setDisable(true);
            gymBtn.setDisable(true);
        }
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
        FadeInDown fadeIn = new FadeInDown(anchorPane);
        //  fadeIn.setSpeed(2);
        fadeIn.setOnFinished(e -> {
            DashboardMenuController controller = loader.getController();
            controller.setMenus(borderPane, sidePane, menuHBox, warningStack);
            controller.setActiveUser(activeUser);
            borderPane.setCenter(anchorPane);
        });
        fadeIn.play();
    }

    @FXML
    void closeHandler() {
        closeStage();
    }

    @FXML
    void logOutHandler() throws IOException {
        closeStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/service/login.fxml"));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();


    }

    private void closeStage() {
        Stage stage = (Stage) activeProfile.getScene().getWindow();
        FadeOut fadeOut = new FadeOut(activeProfile.getParent());
        fadeOut.setSpeed(1);
        fadeOut.setOnFinished(e -> {
            stage.close();
        });
        fadeOut.play();
    }
}
