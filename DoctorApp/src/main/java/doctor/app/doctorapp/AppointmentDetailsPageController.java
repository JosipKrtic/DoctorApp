package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Objects;

public class AppointmentDetailsPageController {

    @FXML
    private Text patient;

    @FXML
    private TextField patientOIB, date, time, duration, symptoms;

    @FXML
    private ImageView timeImage;

    private Appointment appointment;

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
        //set headline text
        patient.setText(appointment.getPatientNameFromAppointment() + "'s appointment");

        //set appointment details text
        patientOIB.setText(String.valueOf(appointment.getPatientOIB()));
        date.setText(appointment.getDate().toString());
        time.setText(appointment.getTime());
        duration.setText(String.valueOf(appointment.getDuration()));
        symptoms.setText(appointment.getSymptoms());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editTime() {
        if (!time.isEditable()) {
            time.setEditable(true);
            timeImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            time.setEditable(false);
            timeImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //call stored procedure to update the time for appointment
            Connection connection = SQLServerConnection.getConnection();
            try {
                String callProcedure = "{call updateTimeForAppointment(?, ?)}";
                try (CallableStatement stmt = connection.prepareCall(callProcedure)) {
                    stmt.setTime(1, Time.valueOf(time.getText() + ":00"));
                    stmt.setInt(2, this.appointment.getID());
                    stmt.execute();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            appointment.setTime(time.getText());
        }
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        Connection connection = SQLServerConnection.getConnection();
        try {
            //stored procedure to delete the selected appointment
            String callProcedure = "{call deleteAppointment(?)}";
            try (CallableStatement stmt = connection.prepareCall(callProcedure)) {
                stmt.setInt(1, appointment.getID());
                stmt.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeWindow(event);
    }
}