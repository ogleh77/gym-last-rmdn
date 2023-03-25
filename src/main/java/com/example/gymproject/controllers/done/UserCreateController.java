package com.example.gymproject.controllers.done;

import animatefx.animation.FadeOut;
import com.example.gymproject.dto.UserService;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserCreateController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton admin;

    @FXML
    private JFXButton createBtn;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstname;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField lastname;

    @FXML
    private JFXRadioButton male;

    @FXML
    private PasswordField oldPassword;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton superAdmin;

    @FXML
    private Label phoneValidation;

    @FXML
    private TextField username;
    @FXML
    private AnchorPane userCreatePane;

    private final ToggleGroup roleToggle = new ToggleGroup();
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            admin.setSelected(true);
            stage = (Stage) username.getScene().getWindow();
        });
        phoneValidation();

        service.setOnSucceeded(e -> {
            createBtn.setGraphic(null);
            createBtn.setText("Created");
            System.out.println(users());
        });
    }

    private Users users() {
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";

        return new Users(0, firstname.getText().trim(), lastname.getText().trim()
                , phone.getText().trim(), gander, shift.getValue().trim(), username.getText().trim(),
                oldPassword.getText().trim(), image, role);

    }

    private void initFields() {
        admin.setSelected(true);
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(super.getShift());

        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, oldPassword);
    }

    private void phoneValidation() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("[^\\d]", ""));
                phoneValidation.setText("Fadlan lanbarka xarfo looma ogola");
                phoneValidation.setVisible(true);
            } else if (!phone.getText().matches("^\\d{7}")) {
                phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
                phoneValidation.setVisible(true);

            } else {
                phoneValidation.setVisible(false);
            }
        });

    }

    @FXML
    void createUserHandler() {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7
                || !phoneValidation.isVisible())) {
            System.out.println(users());
            if (!imageUploaded) {
                checkImage(imageView, "Fadlan sawirka lama helin isku day mar kale");
            }
            if (start) {
                service.restart();
                createBtn.setGraphic(getLoadingImageView());
                createBtn.setText("Creating");
            } else {
                service.start();
                createBtn.setGraphic(getLoadingImageView());
                createBtn.setText("Creating");
                start = true;
            }
        }
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Platform.runLater(() -> {
                            try {
                                UserService.insertUser(users());
                                Platform.runLater(() -> informationAlert("Wxad samaysay user cusub"));
                            } catch (SQLException e) {
                                Platform.runLater(() -> errorMessage(e.getMessage()));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

    @FXML
    void imageUploadHandler() {
        uploadImage(imageView);
    }

    @FXML
    void cancelHandler() {
        FadeOut fadeOut = new FadeOut(userCreatePane);
        fadeOut.setSpeed(2);
        fadeOut.setOnFinished(e -> stage.close());
        fadeOut.play();
    }
}
