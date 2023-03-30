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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUserController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton admin;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstname;

    @FXML
    private TextField idFeild;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField lastname;

    @FXML
    private JFXRadioButton male;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton superAdmin;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private TextField username;
    @FXML
    private Label warningMessage;

    private Users users;
    private final ToggleGroup roleToggle = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initFields);
        service.setOnSucceeded(e -> {
            updateBtn.setText("Updated");
            updateBtn.setGraphic(null);
        });
    }

    private Users users() {
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";

        users.setFirstName(firstname.getText().trim());
        users.setPhone(phone.getText().trim());
        users.setLastName(lastname.getText().trim());
        users.setUsername(username.getText().trim());
        users.setPassword(password.getText().trim());
        users.setImage(image);
        users.setGender(gander);
        users.setRole(role);
        users.setShift(shift.getValue().trim());
        imageUploaded = true;
        return users;
    }


    public void setUser(Users user) {
        this.users = user;
        setUserData(user);
        firstname.setEditable(false);
        lastname.setEditable(false);
        phone.setEditable(false);
        username.setEditable(false);
        password.setEditable(false);
        warningMessage.setVisible(true);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        this.users = activeUser;
        if (!activeUser.getRole().equals("super_admin")) {
            superAdmin.setDisable(true);
            admin.setDisable(true);
            female.setDisable(true);
            male.setDisable(true);
            shift.setItems(super.getShift());
        }

        setUserData(activeUser);
    }


    private void setUserData(Users user) {
        idFeild.setText(String.valueOf(user.getUserId()));
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        phone.setText(user.getPhone());
        shift.setValue(user.getShift());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        if (user.getGender().equals("Male")) {
            male.setSelected(true);
        } else if (user.getGender().equals("Female")) {
            female.setSelected(true);
        }

        if (user.getRole().equals("super_admin")) {
            superAdmin.setSelected(true);
        } else if (user.getRole().equals("admin")) {
            admin.setSelected(true);
        }


        if (user.getImage() != null) {
            try {
                if (user.getImage() != null) {
                    imageView.setImage(new Image(new FileInputStream(
                            user.getImage())));
                    selectedFile = new File(user.getImage());
                    imageUploaded = true;
                }
            } catch (FileNotFoundException e) {
                errorMessage(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void initFields() {
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
    }

    @FXML
    void updateHandler() {
        if (isValid(getMandatoryFields(), null)) {
            if (!imageUploaded)
                checkImage(imageView, "Sawirku wuxuu ku qurxinyaa profile kaaga");
            if (start) {
                service.restart();
                updateBtn.setGraphic(getLoadingImageView());
                updateBtn.setText("Updating");
            } else {
                service.start();
                updateBtn.setGraphic(getLoadingImageView());
                updateBtn.setText("Updating");
                start = true;
            }
        }
    }

    @FXML
    void cancelHandler() {
        close();
    }

    @FXML
    void uploadImageHandler() {
        uploadImage(imageView);
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
                                UserService.update(users());
                                Platform.runLater(() -> informationAlert("You are successfully updated"));
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


    private void close() {
        FadeOut fadeOut = new FadeOut(username.getParent());
        fadeOut.setOnFinished(e -> {
            Stage stage = (Stage) username.getScene().getWindow();
            stage.close();
        });
        fadeOut.setSpeed(2);
        fadeOut.play();
    }
}
