package com.example.gymproject.validatecontrolles.info;

import com.example.gymproject.dto.GymService;
import com.example.gymproject.dto.PaymentService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.entity.Payments;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.helpers.CustomException;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerInfoController extends CommonClass implements Initializable {
    @FXML
    public Label gymTitle;
    @FXML
    private TableColumn<Payments, Double> amountPaid;

    @FXML
    private TableColumn<Payments, Double> discount;

    @FXML
    private TableColumn<Payments, LocalDate> expDate;

    @FXML
    private TableColumn<Payments, String> month;

    @FXML
    private TableColumn<Payments, String> paidBy;

    @FXML
    private TableColumn<Payments, String> paymentDate;

    @FXML
    private TableColumn<Payments, JFXButton> pendingBtn;
    @FXML
    private TableColumn<Payments, String> poxing;
    @FXML
    private TableColumn<Payments, String> running;
    @FXML
    private TableView<Payments> tableView;
    @FXML
    private TableColumn<Payments, String> vipBox;
    @FXML
    private TableColumn<Payments, String> year;
    @FXML
    private ImageView imgView;
    @FXML
    private Label fullName;
    @FXML
    private Label address;
    @FXML
    private Label phone;
    @FXML
    private Label shift;
    @FXML
    private Label weight;
    @FXML
    private Label gander;
    @FXML
    private Label whoAdded;
    private ObservableList<Payments> payments;
    private final Gym currentGym;

    private ButtonType ok;

    private ButtonType cancel;
    private final String pendStyle;
    private final String unPendStyle;

    public CustomerInfoController() throws SQLException {
        this.currentGym = GymService.getGym();
        this.pendStyle = "-fx-background-color: #afd6e3;-fx-text-fill: black;-fx-font-family:Verdana;" + "-fx-pref-width: 100;-fx-font-size: 15";
        this.unPendStyle = "-fx-background-color: red;-fx-text-fill: white;-fx-font-family:Verdana;" + "-fx-pref-width: 100;-fx-font-size: 15";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            if (!payments.isEmpty()) {
                for (Payments payment : payments) {
                    EventHandler<MouseEvent> pending = event -> {
                        try {
                            checkPayment(payment);
                        } catch (SQLException e) {
                            errorMessage(e.getMessage());
                        }
                        tableView.refresh();
                    };
                    payment.getPendingBtn().addEventFilter(MouseEvent.MOUSE_CLICKED, pending);
                }
            }
        });

    }


    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        if (customer != null) {
            fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
            phone.setText(customer.getPhone());
            gander.setText(customer.getGander());
            address.setText(customer.getAddress() == null ? " no address " : customer.getAddress());
            shift.setText(customer.getShift());
            weight.setText(customer.getWeight() + "");
            whoAdded.setText(customer.getWhoAdded());
            try {
                payments = PaymentService.fetchAllCustomersPayments(customer.getPhone());
            } catch (CustomException e) {
                errorMessage(e.getMessage());
            }
//            try {
//                if (customer.getImage() != null) {
//                    imgView.setImage(new Image(new FileInputStream(customer.getImage())));
//                }
//            } catch (FileNotFoundException e) {
//                errorMessage("Sawirka lama helin ama khaladkan ayaa dhacay" + e.getMessage());
//            }
        }
    }


    private void initFields() {
        gymTitle.setText(currentGym.getGymName() + " eDahab: " + currentGym.geteDahab() + " Zaad: " + currentGym.getZaad());

        if (payments.isEmpty()) {
            tableView.setPlaceholder(new Label("MACMIILKU PAYMENTS MALEH.."));
        } else {
            amountPaid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
            discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
            expDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
            month.setCellValueFactory(new PropertyValueFactory<>("month"));
            paidBy.setCellValueFactory(new PropertyValueFactory<>("paidBy"));
            paymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
            pendingBtn.setCellValueFactory(new PropertyValueFactory<>("pendingBtn"));
            poxing.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isPoxing() ? "Yes" : "No"));
            running.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().isOnline() ? "Yes" : "No"));
            vipBox.setCellValueFactory(payment -> new SimpleStringProperty(payment.getValue().getBox() != null ? "Yes" : "No"));
            year.setCellValueFactory(new PropertyValueFactory<>("year"));
            tableView.setItems(payments);
        }
    }

    private void checkPayment(Payments payment) throws SQLException {
        if (payment.isPending()) {
            unPayment(payment);
        } else {
            pendPayment(payment);
        }

    }

    private void pendPayment(Payments payment) throws SQLException {
        LocalDate exp = payment.getExpDate();
        LocalDate pendingDate = LocalDate.now();
        int daysRemind = Period.between(pendingDate, exp).getDays();

        if (ok == null && cancel == null) {
            ok = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
            cancel = new ButtonType("Maya!", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ma hubtaa inaad hakisto paymentkan oo ay u hadhay \n" + "Wakhtigiisa dhicitaanka " + daysRemind + " malmood", ok, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            PaymentService.holdPayment(payment, currentGym.getPendingDate());
            payment.setPending(true);
            payment.setOnline(false);
            payment.getPendingBtn().setText("Fur");
            payment.getPendingBtn().setStyle(unPendStyle);
        } else {
            alert.close();
        }
    }

    private void unPayment(Payments payment) throws SQLException {

        if (ok == null && cancel == null) {
            ok = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
            cancel = new ButtonType("Maya!", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ma hubtaa inaad dib u furto paymentkan ", ok, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            PaymentService.unHoldPayment(payment);
            payment.setPending(false);
            payment.setOnline(true);

            payment.getPendingBtn().setStyle(pendStyle);
            payment.getPendingBtn().setText("Haki");
            payment.setExpDate(payment.getExpDate());
        } else {
            alert.close();
        }
    }
}
