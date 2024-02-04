package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import doctor.app.doctorapp.Models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPrescriptionPageController {

    @FXML
    private TextField doctorID, doctorName, patientOIB, patientName;

    @FXML
    private TextArea medicationList, description;

    @FXML
    private ChoiceBox medicationChoiceBox;

    private int selectedMedicationID;


    public void setDoctor(Doctor doctor) {
        //sets doctor id and name for the new prescription
        doctorID.setText(String.valueOf(doctor.getDoctorID()));
        doctorName.setText(doctor.getFirstName() + " " + doctor.getLastName());
    }

    public void setPatient(Patient patient) {
        //sets patient name and OIB for the new prescription
        patientOIB.setText(String.valueOf(patient.getOIB()));
        patientName.setText(patient.getFirstName() + " " + patient.getLastName());
    }

    public void setMedication() {
        //Creates a list of String objects for medications
        List<String> medicationListFromDB = new ArrayList<>();
        medicationListFromDB.add("Choose medication");
        //get all medications from DB where guide is not null and store them in the list
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Medication WHERE guide IS NOT NULL";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String medication = result.getString("name");
                medicationListFromDB.add(medication);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //add the list of medications to the choiceBox (dropdown list)
        ObservableList<String> medicationList = FXCollections.observableArrayList(medicationListFromDB);
        medicationChoiceBox.setValue("Choose medication");
        medicationChoiceBox.setItems(medicationList);
    }

    @FXML
    private void onMedicationSelected() {
        // Get the selected medication
        String selectedMedication = medicationChoiceBox.getValue().toString();

        // Check if the selected medication is not null and not already in the TextArea
        if (selectedMedication != null && !selectedMedication.equals("Choose medication") && !medicationList.getText().contains(selectedMedication)) {
            // Append the selected medication to the TextArea
            medicationList.appendText(selectedMedication + "\n");
        }
    }


    @FXML
    private void createPrescription(ActionEvent event) {
        Connection connection = SQLServerConnection.getConnection();
        try {
            //find the next possible ID for new prescription
            String sql = "SELECT MAX(ID) FROM Prescription";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            int ID = 0;
            if (result.next()) {
                ID = result.getInt(1) + 1;
            }

            //insert new prescription
            String sqlForPrescription = "INSERT INTO Prescription (ID, description, doctorID, patientOIB) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlForPrescription);

            preparedStatement.setInt(1, ID);
            preparedStatement.setString(2, description.getText());
            preparedStatement.setInt(3, Integer.parseInt(doctorID.getText()));
            preparedStatement.setInt(4, Integer.parseInt(patientOIB.getText()));

            preparedStatement.executeUpdate();

            //convert the TextArea with selected medications to a list of String objects
            List<String> selectedMedications = Arrays.asList(medicationList.getText().split("\n"));

            //for each of the medications from the list, get ID of that medication
            for (String selectedMedication : selectedMedications) {
                String sqlForSelectedMedication = "SELECT ID FROM Medication WHERE name='" + selectedMedication + "'";
                statement = connection.createStatement();
                result = statement.executeQuery(sqlForSelectedMedication);
                if (result.next()) {
                    selectedMedicationID = result.getInt("ID");
                }

                //insert the new row in the table Prescription_Medication (many to many relation)
                String sqlForMedicationPrescriptionConnection = "INSERT INTO Prescription_Medication (prescriptionID, medicationID) VALUES (?, ?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sqlForMedicationPrescriptionConnection);

                preparedStatement2.setInt(1, ID);
                preparedStatement2.setInt(2, selectedMedicationID);

                preparedStatement2.executeUpdate();
            }

            closeWindow(event);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}