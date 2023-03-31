package com.example.gymproject.validatecontrolles.service;

import animatefx.animation.FadeOut;
import com.example.gymproject.dto.main.BackupService;
import com.example.gymproject.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BackupController extends CommonClass implements Initializable {
    @FXML
    private ListView<String> listView;

    @FXML
    private TextField name;
    @FXML
    private Label path;

    @FXML
    private JFXButton backupBtn;
    @FXML
    private JFXButton restoreBtn;
    @FXML
    private JFXButton pathBtn;
    @FXML
    private AnchorPane backupPane;
    private Stage thisStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            thisStage = (Stage) backupBtn.getScene().getWindow();
            getMandatoryFields().add(name);
            try {
                if (!BackupService.backupPaths().isEmpty()) {
                    listView.setItems(BackupService.backupPaths());
                    pathBtn.setDisable(true);
                }
            } catch (SQLException e) {
                errorMessage(e.getMessage());
            }
        });

        backupService.setOnSucceeded(e -> {
            backupBtn.setGraphic(null);
            backupBtn.setText("Back");
            System.out.println("Done");
        });

        restoreService.setOnSucceeded(e -> {
            restoreBtn.setGraphic(null);
            restoreBtn.setText("Restore");
            System.out.println("Done");
        });
    }

    @FXML
    void backupHandler() {
        if (start) {
            backupService.restart();
            backupBtn.setGraphic(getLoadingImageView());
            backupBtn.setText("backuping");
        } else {
            backupService.start();
            backupBtn.setGraphic(getLoadingImageView());
            backupBtn.setText("backuping");
            start = true;
        }


        // TODO: 31/03/2023 Choose backup plase
    }

    @FXML
    void cancelHandler() {
        FadeOut fadeOut = new FadeOut(backupPane);
        fadeOut.setOnFinished(e -> thisStage.close());
        fadeOut.play();
    }

    // TODO: 24/03/2023 Insha Allah retore ka sii hubi 
    @FXML
    void restoreHandler() {
        if (start) {
            restoreService.restart();
            restoreBtn.setGraphic(getLoadingImageView());
            restoreBtn.setText("Restoring");
        } else {
            restoreService.start();
            restoreBtn.setGraphic(getLoadingImageView());
            restoreBtn.setText("Restoring");
            start = true;
        }

    }


    @FXML
    void pathHandler() throws SQLException {
        if (isValid(getMandatoryFields(), null)) {
            pathSelector();
        }
    }

    private void pathSelector() throws SQLException {
        DirectoryChooser chooser = new DirectoryChooser();
        File selectedPath = chooser.showDialog(null);
        BackupService.insertPath(selectedPath.getAbsolutePath() + "/" + name.getText());
        path.setText("Path: " + selectedPath + "/" + name.getText());
        listView.getItems().add(selectedPath.getAbsolutePath() + "/" + name.getText());
    }


    private final Service<Void> backupService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(1000);
                        BackupService.backup(listView.getSelectionModel().getSelectedItem());

                        Platform.runLater(() -> informationAlert("Backuped successfully."));
                    } catch (SQLException e) {
                        Platform.runLater(() -> infoAlert(e.getMessage()));
                        /// e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            };
        }
    };

    private final Service<Void> restoreService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(1000);
                        BackupService.restore(listView.getSelectionModel().getSelectedItem());

                        Platform.runLater(() -> informationAlert("Restored successfully."));
                    } catch (SQLException e) {
                        Platform.runLater(() -> infoAlert(e.getMessage()));
                        /// e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
            };
        }
    };
}
