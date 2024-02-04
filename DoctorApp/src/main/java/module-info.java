module doctor.app.doctorapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens doctor.app.doctorapp to javafx.fxml;
    exports doctor.app.doctorapp;
}