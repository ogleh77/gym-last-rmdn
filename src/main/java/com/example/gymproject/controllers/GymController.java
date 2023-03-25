package com.example.gymproject.controllers;

import animatefx.animation.FadeOut;
import com.example.gymproject.dto.BoxService;
import com.example.gymproject.dto.GymService;
import com.example.gymproject.entity.Box;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.helpers.CustomException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GymController extends CommonClass implements Initializable {
    @FXML
    private TextField addBox;

    @FXML
    private TextField boxCost;

    @FXML
    private TextField eDahab;

    @FXML
    private TextField fitnessCost;

    @FXML
    private TextField gymName;

    @FXML
    private AnchorPane gymPane;

    @FXML
    private ListView<Box> listView;

    @FXML
    private TextField maxDiscount;

    @FXML
    private TextField pendDate;

    @FXML
    private TextField poxingCost;

    @FXML
    private TextField zaad;
    private final Gym currentGym;
    private Stage thisStage;
    int nextId;

    public GymController() throws SQLException {
        currentGym = GymService.getGym();
        nextId = (BoxService.fetchBoxes().get(BoxService.fetchBoxes().size() - 1).getBoxId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initData();
            thisStage = (Stage) poxingCost.getScene().getWindow();
        });
        fitnessValidation();
        poxingValidation();
        discountValidation();
        boxValidation();
        pendValidation();
        zaadValidation();
        eDahabValidation();
    }

    @FXML
    void cancelHandler() {
        FadeOut fadeOut = new FadeOut(gymPane);
        fadeOut.setOnFinished(e -> {
            thisStage.close();
        });
        fadeOut.play();
    }

    @FXML
    void createBoxHandler() throws SQLException {
        nextId++;
        Box box = new Box(nextId, addBox.getText(), true);
        System.out.println(box.getBoxId());
        try {
            BoxService.insertBox(box);
            listView.getItems().add(box);
            BoxService.fetchBoxes().add(box);
            informationAlert("Waxaad diwaan gelisay khanad cusub fadlan ka check garee box view-ga");

        } catch (CustomException e) {
            errorMessage(e.getMessage());
        }
        // TODO: 25/03/2023 Insha Allah  box ka hel next ID giisa si aad u samayso next Box
    }

    @FXML
    void deleteBoxHandler() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            try {
                BoxService.deleteBox(listView.getSelectionModel().getSelectedItem());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
                informationAlert("Waxaad masaxdey khanad");
            } catch (SQLException e) {
                errorMessage(e.getMessage());
            }

        }
    }

    @FXML
    void updateHandler() {
        try {
            double fitness_Cost = Double.parseDouble(fitnessCost.getText());
            double pox_cost = Double.parseDouble(poxingCost.getText());
            double max_discount = Double.parseDouble(maxDiscount.getText());
            int zaad_number = Integer.parseInt(zaad.getText());
            int eDahab_number = Integer.parseInt(eDahab.getText());
            int pend_date = Integer.parseInt(pendDate.getText());
            double box_cost = Double.parseDouble(boxCost.getText());


            currentGym.setGymName(gymName.getText());
            currentGym.seteDahab(zaad_number);
            currentGym.setMaxDiscount(max_discount);
            currentGym.seteDahab(eDahab_number);
            currentGym.setZaad(zaad_number);
            currentGym.setPoxingCost(pox_cost);
            currentGym.setPendingDate(pend_date);
            currentGym.setBoxCost(box_cost);
            currentGym.setMaxDiscount(max_discount);
            currentGym.setFitnessCost(fitness_Cost);

            GymService.updateGym(currentGym);
            informationAlert("Waxaad ku guleysatay update-garaynta gymka  ");
        } catch (Exception e) {
            if (e.getClass().isInstance(SQLException.class)) {
                errorMessage(e.getMessage());
            } else {
                errorMessage("Fadlan hubi inad si saxan u gelisay qimayasha " + "Tusaale 12 AMA 12.0 error caused " + e.getMessage());
            }
        }
    }

    //----------------------Helper methods-------------------
    private void initData() {
        gymName.setText(currentGym.getGymName());
        fitnessCost.setText(String.valueOf(currentGym.getFitnessCost()));
        boxCost.setText(String.valueOf(currentGym.getBoxCost()));
        poxingCost.setText(String.valueOf(currentGym.getPoxingCost()));
        maxDiscount.setText(String.valueOf(currentGym.getMaxDiscount()));
        eDahab.setText((String.valueOf(currentGym.geteDahab())));
        zaad.setText((String.valueOf(currentGym.getZaad())));
        listView.setItems(currentGym.getVipBoxes());
        pendDate.setText(String.valueOf(currentGym.getPendingDate()));
    }

    private void poxingValidation() {
        poxingCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                poxingCost.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
                System.out.println("Valid");
            } else {
                System.out.println("Invalid");
            }
        });
    }

    private void boxValidation() {
        boxCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                boxCost.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
                System.out.println("Valid");
            } else {
                System.out.println("Invalid");
            }
        });
    }

    private void pendValidation() {
        System.out.println("Validating pend");
        pendDate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pendDate.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void discountValidation() {
        System.out.println("Validating max");

        maxDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxDiscount.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
            }
        });
    }

    private void fitnessValidation() {
        System.out.println("Validating fitness");

        fitnessCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                fitnessCost.setText(newValue.replaceAll("[^\\d\\.?}]", ""));
            }
        });
    }

    private void zaadValidation() {
        System.out.println("Validating pend");
        zaad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                zaad.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void eDahabValidation() {
        System.out.println("Validating pend");
        eDahab.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                eDahab.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}
