package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import doctor.app.doctorapp.Models.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PrescriptionPageController {

    @FXML
    private Text patientHeadline;

    @FXML
    private TableView<ObservableList<String>> prescriptionTable;
    @FXML
    private TableColumn<ObservableList<String>, String> doctorColumn, patientColumn, medicationColumn, descriptionColumn;

    private Doctor doctor;
    private Patient patient;


    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        //set the headline text
        patientHeadline.setText(patient.getFirstName() + " " + patient.getLastName() + "'s prescriptions");

        StringBuilder medications = new StringBuilder();
        List<Integer> medicationIdList = new ArrayList<>();
        ObservableList<ObservableList<String>> prescriptionData = FXCollections.observableArrayList();

        //to set the values to the prescription table
        Connection connection = SQLServerConnection.getConnection();
        try {
            //get ID of prescription with provided doctor ID and patient ID
            String sql = "SELECT * FROM Prescription WHERE doctorID='" + doctor.getDoctorID() + "' AND patientOIB='" + patient.getOIB() + "'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("ID");
                //get all the medications IDs from Prescription_Medication table with provided prescription ID
                String sqlForMedications = "SELECT * FROM Prescription_Medication WHERE prescriptionID=" + ID;
                Statement newStatement = connection.createStatement();
                ResultSet newResultSet = newStatement.executeQuery(sqlForMedications);
                while (newResultSet.next()) {
                    int medicationID = newResultSet.getInt("medicationID");
                    //add all the medication IDs to the list
                    medicationIdList.add(medicationID);
                }
                //for each medication ID from the list get the medication name and append it to the string
                for (Integer med : medicationIdList) {
                    String sqlForMedicationList = "SELECT * FROM Medication WHERE ID=" + med;
                    Statement newestStatement = connection.createStatement();
                    ResultSet newestResult = newestStatement.executeQuery(sqlForMedicationList);
                    while (newestResult.next()) {
                        String medication = newestResult.getString("name");
                        medications.append(medication).append(", ");
                    }
                }

                //to remove the ", " from the end of the string
                medications = new StringBuilder(medications.substring(0, medications.length() - 2));

                String description = result.getString("description");
                prescriptionData.add(FXCollections.observableArrayList(Arrays.asList(doctor.getFirstName() + " " + doctor.getLastName(),
                        patient.getFirstName() + " " + patient.getLastName(), medications.toString(), description)));
                medications = new StringBuilder();
                medicationIdList.clear();
            }
            prescriptionTable.setItems(prescriptionData);
            doctorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
            patientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
            medicationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
            descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void createNewPrescription() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AddPrescriptionPage.fxml"));
        Parent root = loader.load();
        AddPrescriptionPageController addPrescriptionPageController = loader.getController();
        addPrescriptionPageController.setDoctor(doctor);
        addPrescriptionPageController.setPatient(patient);
        addPrescriptionPageController.setMedication();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Create prescription");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}