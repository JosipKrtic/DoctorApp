package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class AddPatientPageController {

    @FXML
    private TextField OIB, password, firstName, lastName, age, sex, address, contact, bloodType, height, weight, insurance;

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

    @FXML
    private void addPatient(ActionEvent event) throws SQLException {
        //check if all the fields are entered
        if (validation(Arrays.asList(OIB, password, firstName, lastName, age, sex, address, contact, bloodType,
                height, weight, insurance))) {
            //to find the next possible id (max+1) for new medical record
            Connection connection = SQLServerConnection.getConnection();
            String sqlForMedicalRecordID = "SELECT MAX(ID) FROM MedicalRecord";
            Statement newStatement = connection.createStatement();
            ResultSet newResult = newStatement.executeQuery(sqlForMedicalRecordID);
            int ID = 0;
            if (newResult.next()) {
                ID = newResult.getInt(1) + 1;
            }
            //to create new medical record, conditions always set to 0000... = no entry
            String sqlForNewMedicalRecord = "INSERT INTO MedicalRecord (ID, conditions) VALUES (?, ?)";
            PreparedStatement preparedStatementForMedicalRecord = connection.prepareStatement(sqlForNewMedicalRecord);
            preparedStatementForMedicalRecord.setInt(1, ID);
            preparedStatementForMedicalRecord.setString(2, "0000000000000000000000000000000000000000");
            preparedStatementForMedicalRecord.executeUpdate();

            //calls stored procedure "sifriraj" to encrypt the password and insert the new Patient
            String callSifrirajProcedure = "{CALL sifriraj(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement statement = connection.prepareCall(callSifrirajProcedure)) {
                statement.setInt(1, Integer.parseInt(OIB.getText()));
                statement.setString(2, password.getText());
                statement.setString(3, firstName.getText());
                statement.setString(4, lastName.getText());
                statement.setInt(5, Integer.parseInt(age.getText()));
                statement.setString(6, sex.getText());
                statement.setString(7, address.getText());
                statement.setString(8, contact.getText());
                statement.setString(9, bloodType.getText());
                statement.setInt(10, Integer.parseInt(weight.getText()));
                statement.setInt(11, Integer.parseInt(height.getText()));
                statement.setInt(12, ID);
                statement.setInt(13, Integer.parseInt(insurance.getText()));
                statement.setInt(14, doctor.getDoctorID());
                statement.execute();

                closeWindow(event);
            } catch (SQLException ex) {
                //to catch exception for invalid age (<0) defined by constraint in DB
                if (ex.getMessage().contains("The INSERT statement conflicted with the CHECK constraint " +
                        "\"Positive_Patient_Age\". The conflict occurred in database \"DoctorApp\", table " +
                        "\"dbo.Patient\", column 'age'.")) {
                    System.out.println("The age on patient can't be negative value!");
                    age.setBorder(new Border(redBorder));
                //to catch exception for invalid first and/or last name defined by index in DB
                } else if (ex.getMessage().contains("Invalid name format. Names must start with a capital letter " +
                        "followed by lowercase letters.")) {
                    System.out.println("First name and last name need to start with a capital letter!");
                    firstName.setBorder(new Border(redBorder));
                    lastName.setBorder(new Border(redBorder));
                } else {
                    ex.printStackTrace();
                }
            }
        }
    }

    private boolean validation(List<TextField> inputs) {
        boolean success = true;
        for (TextField input : inputs) {
            if (input.getText().equals("")) {
                input.setBorder(new Border(redBorder));
                success = false;
            } else {
                input.setBorder(new Border(noBorder));
            }
        }
        return success;
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}