package com.example.gymproject.validatecontrolles.service;


import com.example.gymproject.HelloApplication;
import com.example.gymproject.dto.services.CustomerService;
import com.example.gymproject.entity.Customers;
import com.example.gymproject.entity.Payments;
import com.example.gymproject.entity.Users;
import com.example.gymproject.helpers.CommonClass;
import com.example.gymproject.validatecontrolles.DashboardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SplashScreenController extends CommonClass implements Initializable {
    @FXML
    private ProgressBar progress;
    @FXML
    private Label waiting;
    @FXML
    private Label welcomeUserName;
    @FXML
    private ImageView loadingImage;

    private final ObservableList<Customers> warningList;

    public SplashScreenController() {
        this.warningList = FXCollections.observableArrayList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FetchOnlineCustomersByGander.setOnSucceeded(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymproject/views/dashboard.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load());
                DashboardController controller = fxmlLoader.getController();
                controller.setWarningList(warningList);
                controller.setActiveUser(activeUser);
                Stage stage = new Stage(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public Task<Void> FetchOnlineCustomersByGander = new Task<>() {
        private final LocalDate now = LocalDate.now();

        @Override
        protected Void call() throws Exception {
            int i = 0;
            ObservableList<Customers> offlineCustomers = CustomerService.fetchOnlineCustomer(activeUser);
            System.out.println("Warning " + offlineCustomers);
            //----Check-----
            for (Customers customer : offlineCustomers) {
                i++;
                updateMessage("Loading.. " + i + "%");
                updateProgress(i, offlineCustomers.size());
                for (Payments payment : customer.getPayments()) {
                    LocalDate expDate = payment.getExpDate();
                    if (now.plusDays(2).isEqual(expDate) || now.plusDays(1).isEqual(expDate) || now.isEqual(expDate)) {
                        warningList.add(customer);
                        System.out.println(customer.getFirstName() + " " + expDate + " Warning");
                    } else if (now.isBefore(expDate)) {
                        System.out.println(customer.getFirstName() + " " + expDate + " Is active");
                    } else {
                        // TODO: 05/04/2023 Make the payment of
                        System.out.println(customer.getFirstName() + " " + expDate + " Outdated");
                    }
                }
                Thread.sleep(100);
            }
            return null;
        }
    };

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        Thread thread = new Thread(FetchOnlineCustomersByGander);
        thread.setDaemon(true);
        thread.start();
        progress.progressProperty().bind(FetchOnlineCustomersByGander.progressProperty());
        welcomeUserName.setText("Welcome " + activeUser.getUsername());
        waiting.textProperty().bind(FetchOnlineCustomersByGander.messageProperty());

        URL url = getClass().getResource(activeUser.getGender().equals("Male") ? images[1] : images[2]);
        Image image = new Image(String.valueOf(url));
        loadingImage.setImage(image);


    }

}
