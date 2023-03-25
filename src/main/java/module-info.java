module com.example.gymproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires com.jfoenix;
    requires junit;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.gymproject to javafx.fxml;
    opens com.example.gymproject.controllers to javafx.fxml;

    exports com.example.gymproject;
}