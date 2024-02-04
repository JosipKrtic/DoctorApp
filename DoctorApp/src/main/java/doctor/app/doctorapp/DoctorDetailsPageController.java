package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DoctorDetailsPageController {

    @FXML
    private Text doctorText;

    @FXML
    private TextField firstName, lastName, contact, hospital;


    public void setDoctor(Doctor doctor, String hospitalName) {
        doctorText.setText(doctor.getFirstName() + " " + doctor.getLastName());
        firstName.setText(doctor.getFirstName());
        lastName.setText(doctor.getLastName());
        contact.setText(doctor.getContact());
        hospital.setText(hospitalName);
    }
    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}