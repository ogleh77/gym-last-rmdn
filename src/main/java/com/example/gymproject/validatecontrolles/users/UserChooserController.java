package com.example.gymproject.validatecontrolles.users;

import animatefx.animation.FadeOut;
import com.example.gymproject.controllers.done.UpdateUserController;
import com.example.gymproject.dto.UserService;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.helpers.CustomException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserChooserController extends CommonClass implements Initializable {
    @FXML
    private ListView<Users> listView;
    private Stage thisStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            try {
                initFields();
                thisStage = (Stage) listView.getScene().getWindow();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void cancelHandler() {
        close();
    }

    private void initFields() throws SQLException {
        listView.setItems(UserService.users());
    }

    @FXML
    void updateHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new CustomException("Marka soo dooro userka aad rabto inaad update-garyso.");
            }

            FadeOut fadeOut = new FadeOut(listView.getParent());

            fadeOut.setOnFinished(e -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymproject/views/users/user-update.fxml"));
                try {
                    Scene scene = new Scene(loader.load());
                    UpdateUserController controller = loader.getController();
                    controller.setUser(listView.getSelectionModel().getSelectedItem());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                } catch (IOException ex) {
                    errorMessage(ex.getMessage());
                }
                thisStage.close();
            });
            fadeOut.setSpeed(2);
            fadeOut.play();
        } catch (Exception e) {
            infoAlert(e.getMessage());
        }

    }

    @FXML
    void deleteHandler() throws CustomException {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new CustomException("Marka hore soo dooro userka aad rabto inaad delete-garyso.");
            }
            confirmDelete(listView.getSelectionModel().getSelectedItem().getUsername());
        } catch (Exception e) {
            if (e instanceof SQLException) {
                errorMessage(e.getMessage());
            } else {
                infoAlert(e.getMessage());
            }
        }


    }

    private void confirmDelete(String username) throws SQLException {

        ButtonType okBtn = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Maya", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ma hubtaa inaad delete garayso " + "userka " + username, okBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().equals(okBtn)) {
            UserService.delete(listView.getSelectionModel().getSelectedItem());
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        } else {
            System.out.println("Cancel");
            alert.close();
        }
    }

    private void close() {
        FadeOut fadeOut = new FadeOut(listView.getParent());
        fadeOut.setSpeed(2);
        fadeOut.setOnFinished(e -> {
            thisStage.close();
        });
        fadeOut.play();
    }
    // TODO: 26/03/2023 Ku dar confirmation for chek update insha Allah
}
