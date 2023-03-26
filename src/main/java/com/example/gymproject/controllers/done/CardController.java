package com.example.gymproject.controllers.done;

import com.example.gymproject.entity.Customers;
import com.example.gymproject.helpers.CommonClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

public class CardController extends CommonClass implements Initializable {
    @FXML
    public Label shift;
    @FXML
    private ImageView customerPhoto;
    @FXML
    private Label fullName;
    @FXML
    private Label lastPaid;
    @FXML
    private Label outDated;
    @FXML
    private Label phone;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        LocalDate exp = customer.getPayments().get(0).getExpDate();
        LocalDate today = LocalDate.now();
        Period period = Period.between(exp, today);

        fullName.setText(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        lastPaid.setText(customer.getPayments().get(0).getPaymentDate());


        if (period.getYears() > 0) {
            outDated.setText(period.getYears() == 1 ? period.getYears() + " year ago " : period.getYears() + " years ago");
        } else if (period.getMonths() > 0) {
            outDated.setText(period.getMonths() == 1 ? period.getMonths() + " month ago" : period.getMonths() + " months ago");
        } else if (period.getDays() == 0) {
            outDated.setText("today is left");
        } else if (period.getDays() < 0) {
            outDated.setText(Math.abs(period.getDays()) + " days left");
        } else {
            outDated.setText(period.getDays() == 1 ? period.getDays() + " day ago" : period.getDays() + " days ago");
        }

        System.out.println(customer.getFirstName() + " " + period);
        phone.setText(customer.getPhone());
        shift.setText(customer.getShift());
        try {
            if (customer.getImage() != null) {
                customerPhoto.setImage(new Image(new FileInputStream(customer.getImage())));
            }
        } catch (FileNotFoundException e) {
            errorMessage(e.getMessage());
        }


        // TODO: 24/03/2023 Message   print insha Allah e.g             outDated.setText(period.getYears() == 1 ? period.getYears() + " year " + period.getMonths() + " months and " + period.getYears() + " days ago " : period.getYears() + " years ago");
    }


    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }


}
