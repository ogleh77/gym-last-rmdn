package com.example.gymproject.controllers.done;

import com.example.gymproject.dto.CustomerService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Gym;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController extends CommonClass implements Initializable {
    @FXML
    private TextField address;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstName;

    @FXML
    private Label gymTitle;

    @FXML
    private Label headerInfo;

    @FXML
    private ImageView imgView;

    @FXML
    private TextField lastName;

    @FXML
    private JFXRadioButton male;

    @FXML
    private TextField middleName;

    @FXML
    private TextField phone;

    @FXML
    private Label phoneValidation;

    @FXML
    private JFXButton registerBtn;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private TextField weight;

    @FXML
    private Label weightValidation;
    private boolean isCustomerNew = true;
    private ObservableList<Customers> customersList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            shift.setItems(getShift());
            male.setToggleGroup(genderGroup);
            female.setToggleGroup(genderGroup);
            getMandatoryFields().addAll(firstName, middleName, lastName, phone, shift);
        });

        phoneValidation();
        weightValidation();
        service.setOnSucceeded(e -> {
            registerBtn.setGraphic(null);
            registerBtn.setText(isCustomerNew ? "Saved" : "Updated");
            System.out.println("Done");
        });
    }

    @FXML
    void customerSaveHandler() {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7 || !phoneValidation.isVisible()) && (weight.getText().length() == 2 || !weightValidation.isVisible())) {
            if (!imageUploaded) {
                checkImage(imgView, "Fadlan sawirku wuu kaa cawinayaa inaad wejiga \n" + "macmiilka ka dhex garan kartid macamisha kle");
            }
            if (start) {
                service.restart();
                registerBtn.setGraphic(getLoadingImageView());
                registerBtn.setText(isCustomerNew ? "Saving" : "Updating");
            } else {
                service.start();
                registerBtn.setGraphic(getLoadingImageView());
                registerBtn.setText(isCustomerNew ? "Saving" : "Updating");
                start = true;
            }
        }
    }

    @FXML
    void uploadImageHandler() {
        uploadImage(imgView);
    }

    //----------------________Helper methods_______---------------------
    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);

        if (customer != null) {
            firstName.setText(customer.getFirstName());
            middleName.setText(customer.getMiddleName());
            lastName.setText(customer.getLastName());
            phone.setText(customer.getPhone());
            weight.setText(String.valueOf(customer.getWeight()));
            shift.setValue(customer.getShift());
            address.setText(customer.getAddress() != null ? customer.getAddress() : "No address");
            if (customer.getGander().equals("Male")) {
                male.setSelected(true);
            } else {
                female.setSelected(true);
            }
            weight.setText(String.valueOf(customer.getWeight()).substring(0, 2));
            shift.setValue(customer.getShift());
            address.setText(customer.getAddress() != null ? customer.getAddress() : "No address");
            try {
                if (customer.getImage() != null) {
                    imageUploaded = true;
                    imgView.setImage(new Image(new FileInputStream(customer.getImage())));
                    selectedFile = new File(customer.getImage());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            headerInfo.setText("CUSTOMER UPDATE PAGE");
            isCustomerNew = false;
            registerBtn.setText("Update");
        }
    }

    @Override
    public void setCurrentGym(Gym currentGym) {
        super.setCurrentGym(currentGym);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        if (customer == null) {
            try {
                customersList = CustomerService.fetchAllCustomer(activeUser);
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        CustomerService.insertOrUpdateCustomer(savingCustomer(), isCustomerNew);
                        if (isCustomerNew) {
                            customersList.add(0, savingCustomer());
                        }
                        Thread.sleep(1000);
                        Platform.runLater(() -> informationAlert(isCustomerNew ? "Waxaad diwaan gelisay macmiil cusub" : "Waxaad update garaysay macmiil"));
//                        } else {
                        // customersList.set(CustomerService.binarySearch(customersList, 0, customersList.size() - 1, customer.getCustomerId()), customer);
                        // }
                    } catch (Exception e) {
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                        e.printStackTrace();
                    }
                    return null;
                }
            };
        }
    };

    private Customers savingCustomer() {
        String gander = male.isSelected() ? "Male" : "Female";
        String _address = address.getText() != null ? address.getText().trim() : "No address";
        double _weight = ((!weight.getText().isEmpty() || !weight.getText().isBlank())) ? Double.parseDouble(weight.getText().trim()) : 65.0;
        String image = selectedFile != null ? selectedFile.getAbsolutePath() : null;
        int customerId = super.customer == null ? 0 : customer.getCustomerId();
        String _shift = shift.getValue() != null ? shift.getValue() : "Morning";

        if (customer == null) {
            customer = new Customers(0, firstName.getText().trim(), lastName.getText().trim(), middleName.getText().trim(), phone.getText().trim(), gander, _shift, _address, image, _weight, activeUser.getUsername());
        } else {
            customer.setShift(_shift);
            customer.setCustomerId(customerId);
            customer.setFirstName(firstName.getText().trim());
            customer.setGander(gander);
            customer.setWhoAdded(activeUser.getUsername());
            customer.setImage(image);
            customer.setAddress(_address.trim());
            customer.setLastName(lastName.getText().trim());
            customer.setMiddleName(middleName.getText().trim());
            customer.setPhone(phone.getText().trim());
            customer.setWeight(_weight);
        }
        return customer;
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

    private void weightValidation() {
        weight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                weight.setText(newValue.replaceAll("[^\\d]", ""));
                weightValidation.setText("Fadlan misaanka xarfo looma ogola");
                weightValidation.setVisible(true);
            } else if (!weight.getText().matches("^\\d{0,2}")) {
                weightValidation.setText("Ma jiro qof boaqol sano ka badan");
                weightValidation.setVisible(true);
            } else {
                weightValidation.setVisible(false);
            }
        });

    }


}
