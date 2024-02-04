package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import doctor.app.doctorapp.Models.Patient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PatientListPageController implements Initializable {

    @FXML
    private Text hospitalName;

    @FXML
    private TableView<ObservableList<String>> patientTable;
    @FXML
    private TableColumn<ObservableList<String>, String> OIBColumn, firstNameColumn, lastNameColumn, ageColumn,
            sexColumn, bloodTypeColumn, contactColumn, insuranceColumn;

    @FXML
    private Button homeButton, patientsButton, appointmentsButton, accountButton;

    private Doctor doctor;

    List<Patient> patientList = new ArrayList<>();


    public void setDoctor(Doctor doctor, String hospital) {
        this.doctor = doctor;
        //set hospital name
        hospitalName.setText(hospital);

        //to add patients to the list of patients that are connected to this doctor
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Patient WHERE doctorID=" + doctor.getDoctorID();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int OIB = result.getInt("OIB");
                String passwordDB = result.getString("password");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                int age = result.getInt("age");
                String sex = result.getString("sex");
                String address = result.getString("address");
                String contact = result.getString("contact");
                String bloodType = result.getString("bloodType");
                int weight = result.getInt("weight");
                int height = result.getInt("height");
                int medicalRecordID = result.getInt("medicalRecordID");
                int insuranceID = result.getInt("insuranceID");
                patientList.add(new Patient(OIB, passwordDB, firstName, lastName, age, sex, address, contact,
                        bloodType, weight, height, medicalRecordID, insuranceID, doctor.getDoctorID()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //to add patient to table view
        ObservableList<ObservableList<String>> patientData = FXCollections.observableArrayList();
        for (Patient patient : patientList) {
            patientData.add(FXCollections.observableArrayList(patient.getPatientRowForPatientListView()));
        }

        patientTable.setItems(patientData);

        OIBColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        sexColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        bloodTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        contactColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
        insuranceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(7)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Highlight "Patients" in navigation
        patientsButton.setStyle("-fx-background-color: #44A5FF; -fx-border-color: black;");
    }

    @FXML
    private void goToHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/DoctorHomePage.fxml"));
        Parent root = loader.load();
        HomePageController homePageController = loader.getController();
        homePageController.setDoctor(doctor);
        Scene scene = new Scene(root);
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToPatientListPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/PatientListPage.fxml"));
        Parent root = loader.load();
        PatientListPageController patientListPageController = loader.getController();
        patientListPageController.setDoctor(doctor, hospitalName.getText());
        Scene scene = new Scene(root);
        Stage stage = (Stage) patientsButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAppointmentListPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AppointmentListPage.fxml"));
        Parent root = loader.load();
        AppointmentListPageController appointmentListPageController = loader.getController();
        appointmentListPageController.setDoctor(doctor, hospitalName.getText());
        Scene scene = new Scene(root);
        Stage stage = (Stage) appointmentsButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAccountPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AccountPage.fxml"));
        Parent root = loader.load();
        AccountPageController accountPageController = loader.getController();
        accountPageController.setDoctor(doctor, hospitalName.getText());
        Scene scene = new Scene(root);
        Stage stage = (Stage) accountButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void addPatientPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AddPatientPage.fxml"));
        Parent root = loader.load();
        AddPatientPageController addPatientPageController = loader.getController();
        addPatientPageController.setDoctor(doctor);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("New patient");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void openPatientDetails() {
        patientTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) { // Check for double click
                List<String> selectedItem = patientTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String patientFirstName = selectedItem.get(1);
                    String patientLastName = selectedItem.get(2);
                    String patientFullName = patientFirstName + " " + patientLastName;
                    for (Patient patient : patientList) {
                        if ((patient.getFirstName() + " " + patient.getLastName()).equals(patientFullName)) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/PatientDetailsPage.fxml"));
                            Parent root;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            PatientDetailsPageController patientDetailsPageController = loader.getController();
                            patientDetailsPageController.setDoctor(doctor);
                            patientDetailsPageController.setPatient(patient);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.setTitle("Patient details");
                            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
                            stage.show();
                        }
                    }
                }
            }
        });
    }
}