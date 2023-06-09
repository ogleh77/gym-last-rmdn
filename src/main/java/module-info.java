module com.example.gymproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.jfoenix;
    requires junit;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.gymproject to javafx.fxml;
     opens com.example.gymproject.validatecontrolles to javafx.fxml;
     opens com.example.gymproject.validatecontrolles.main to javafx.fxml;
    opens com.example.gymproject.validatecontrolles.users to javafx.fxml;
    opens com.example.gymproject.validatecontrolles.service to javafx.fxml;


    exports com.example.gymproject;
    exports com.example.gymproject.entity;

    opens com.example.gymproject.validatecontrolles.info to javafx.fxml;
    // opens com.example.gymproject.validatecontrolles.main to javafx.fxml;
}