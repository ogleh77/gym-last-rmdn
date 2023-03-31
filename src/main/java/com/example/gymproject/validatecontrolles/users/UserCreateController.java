package com.example.gymproject.validatecontrolles.users;

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
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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

    private final ToggleGroup roleToggle = new ToggleGroup();
    private Stage stage;
    private boolean done = false;

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

            if (done) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User Created successfully", ButtonType.OK);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get().getButtonData().isDefaultButton()) {
                    stage.close();
                }
            }
        });
    }

    private Users users() throws SQLException {
        int nextId = UserService.predictNextId();
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";
        // TODO: 30/03/2023 Delete currently inserted user
        return new Users(nextId, firstname.getText().trim(), lastname.getText().trim(), phone.getText().trim(), gander,
                shift.getValue().trim(), username.getText().trim(),
                oldPassword.getText().trim(), selectedFile == null ? null : readFile(selectedFile.getAbsolutePath()), role);
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
    void createUserHandler() throws SQLException {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7 || !phoneValidation.isVisible())) {
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
                                UserService.users().add(users());
                                done = true;
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
        FadeOut fadeOut = new FadeOut(username.getParent());
        fadeOut.setSpeed(2);
        fadeOut.setOnFinished(e -> stage.close());
        fadeOut.play();
    }

    private byte[] readFile(String file) {

        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1; ) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;

    }

}
