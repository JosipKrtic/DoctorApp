package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class AddAppointmentPageController {

    @FXML
    private TextField patientOIB, time, duration, symptoms;

    @FXML
    private DatePicker date;

    private Doctor doctor;

    BorderStroke redBorder = new BorderStroke(
            Color.RED, BorderStrokeStyle.SOLID, null, null
    );

    BorderStroke noBorder = new BorderStroke(
            Color.rgb(217, 217, 217), BorderStrokeStyle.SOLID, null, null
    );

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatientOIB(String OIB) {
        patientOIB.setText(OIB);
        patientOIB.setEditable(false);
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void createAppointment(ActionEvent event) {
        //check if all the fields are entered
        if (validation(Arrays.asList(patientOIB, time, duration, symptoms), date)) {
            //find the patient with current OIB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sqlForPatientOIB = "SELECT 1 FROM Patient WHERE OIB=" + patientOIB.getText() + " AND doctorID=" + doctor.getDoctorID();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sqlForPatientOIB);
                if (result.next()) {
                    //find the next possible (max+1) ID for new appointment
                    String sql = "SELECT MAX(ID) FROM Appointment";
                    statement = connection.createStatement();
                    result = statement.executeQuery(sql);
                    int ID = 0;
                    if (result.next()) {
                        ID = result.getInt(1) + 1;
                    }
                    //tp insert new appointment with entered data
                    String sqlForAppointment = "INSERT INTO Appointment (ID, date, time, duration, symptoms, patientOIB, doctorID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlForAppointment);

                    preparedStatement.setInt(1, ID);
                    preparedStatement.setDate(2, java.sql.Date.valueOf(date.getValue()));
                    preparedStatement.setString(3, time.getText());
                    preparedStatement.setInt(4, Integer.parseInt(duration.getText()));
                    preparedStatement.setString(5, symptoms.getText());
                    preparedStatement.setInt(6, Integer.parseInt(patientOIB.getText()));
                    preparedStatement.setInt(7, doctor.getDoctorID());

                    preparedStatement.executeUpdate();
                    closeWindow(event);
                } else {
                    patientOIB.setBorder(new Border(redBorder));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean validation(List<TextField> inputs, DatePicker date) {
        boolean success = true;
        for (TextField input : inputs) {
            if (input.getText().equals("")) {
                input.setBorder(new Border(redBorder));
                success = false;
            } else {
                input.setBorder(new Border(noBorder));
            }
        }
        if (date.getValue() == null) {
            date.setBorder(new Border(redBorder));
            success = false;
        } else {
            date.setBorder(new Border(noBorder));
        }
        return success;
    }
}