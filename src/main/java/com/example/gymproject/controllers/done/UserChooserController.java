package com.example.gymproject.controllers.done;

import animatefx.animation.FadeOut;
import com.example.gymproject.dto.UserService;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserChooserController extends CommonClass implements Initializable {
    @FXML
    private ListView<Users> listView;
    private Stage thisStage;
    @FXML
    private AnchorPane choserPane;

    public UserChooserController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            try {
                initFields();
                thisStage = (Stage) choserPane.getScene().getWindow();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initFields() throws SQLException {
        listView.setItems(UserService.users());
    }

    @FXML
    void updateHandler() {
        FadeOut fadeOut = new FadeOut(choserPane);
        fadeOut.setOnFinished(e -> {
            thisStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/done/user-update.fxml"));
            try {
                Scene scene = new Scene(loader.load());
                UpdateUserController controller = loader.getController();
                controller.setUser(listView.getSelectionModel().getSelectedItem());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        fadeOut.setDelay(Duration.millis(100));
        fadeOut.play();
        System.out.println(listView.getSelectionModel().getSelectedItems());
    }

    @FXML
    void deleteHandler() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            confirmDelete(listView.getSelectionModel().getSelectedItem().getUsername());
        }
    }

    private void confirmDelete(String username) {

        ButtonType okBtn = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Maya", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ma hubtaa inaad delete garayso " + "userka " + username, okBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().equals(okBtn)) {
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            try {
                UserService.delete(listView.getSelectionModel().getSelectedItem());
            } catch (SQLException e) {
                errorMessage(e.getMessage());
            }
        } else {
            System.out.println("Cancel");
            alert.close();
        }
    }

    // TODO: 26/03/2023 Ku dar confirmation for chek update insha Allah
}
