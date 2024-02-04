package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Appointment;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PatientHomePageController implements Initializable {

    @FXML
    private Text patientName, hospitalName;
    @FXML
    private TextField OIB, password, age, sex, address, contact, bloodType, height, weight, insurance;
    @FXML
    private ImageView ageImage, contactImage, addressImage;
    @FXML
    private TableView<ObservableList<String>> appointmentTable;
    @FXML
    private TableColumn<ObservableList<String>, String> symptomsColumn, dateColumn, timeColumn;
    @FXML
    private Button patientButton, logoutButton;
    @FXML
    private TableColumn<ObservableList<String>, String> doctorColumn, patientColumn, descriptionColumn;
    @FXML
    private TableView<ObservableList<String>> prescriptionTable;

    private Doctor doctor;
    private Patient patient;

    List<Appointment> appointmentList = new ArrayList<>();

    public void setPatient(Patient patient) {
        this.patient = patient;
        OIB.setText(String.valueOf(this.patient.getOIB()));
        password.setText(this.patient.getPassword());
        age.setText(String.valueOf(this.patient.getAge()));
        sex.setText(this.patient.getSex());
        address.setText(this.patient.getAddress());
        contact.setText(this.patient.getContact());
        bloodType.setText(this.patient.getBloodType());
        height.setText(String.valueOf(this.patient.getHeight()));
        weight.setText(String.valueOf(this.patient.getWeight()));
        //to set the name/type of insurance by insuranceID from Patient table
        String typeOfInsurance = "";
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Insurance WHERE ID=" + patient.getInsuranceID();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            if (result.next()) {
                typeOfInsurance = result.getString("typeOfInsurance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        insurance.setText(typeOfInsurance);

        //set the headline text for logged in patient
        patientName.setText("Welcome back, " + this.patient.getFirstName() + " " + this.patient.getLastName());

        //set Doctor related to the current Patient
        try {
            String sql = "SELECT * FROM Doctor WHERE doctorID=" + this.patient.getDoctorID();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("doctorID");
                String password = result.getString("password");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String contact = result.getString("contact");
                int hospitalID = result.getInt("IdHospital");
                this.doctor = new Doctor(ID, password, firstName, lastName, contact, hospitalID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //set the hospital name
        try {
            String sql = "SELECT * FROM Hospital";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("ID");
                String name = result.getString("name");
                if (this.doctor.getHospitalID() == ID) {
                    hospitalName.setText(name);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //to add appointments to the list of appointments that are connected to this patient
        try {
            String sql = "SELECT * FROM Appointment WHERE patientOIB=" + this.patient.getOIB();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("ID");
                Date date = result.getDate("date");
                String timeExtended = result.getString("time");
                String time = timeExtended.substring(0, Math.min(timeExtended.length(), 5));
                int duration = result.getInt("duration");
                String symptoms = result.getString("symptoms");
                int patientOIB = result.getInt("patientOIB");
                int doctorID = result.getInt("doctorID");
                appointmentList.add(new Appointment(ID, date, time, duration, symptoms, patientOIB, doctorID));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //to add appointments to table view
        ObservableList<ObservableList<String>> appointmentData = FXCollections.observableArrayList();

        for (Appointment appointment : appointmentList) {
            appointmentData.add(FXCollections.observableArrayList(appointment.getAppointmentRowForPatient()));
        }

        appointmentTable.setItems(appointmentData);

        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        symptomsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));

        //to get all the prescriptions related to the current Patient
        try {
            String sql = "SELECT * FROM Prescription WHERE doctorID='" + this.doctor.getDoctorID() + "' AND patientOIB='" + this.patient.getOIB() + "'";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String description = result.getString("description");
                ObservableList<ObservableList<String>> prescriptionData = FXCollections.observableArrayList();
                prescriptionData.add(FXCollections.observableArrayList(Arrays.asList(doctor.getFirstName() + " " + doctor.getLastName(),
                        patient.getFirstName() + " " + patient.getLastName(), description)));

                prescriptionTable.setItems(prescriptionData);

                doctorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
                patientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
                descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //highlight "Patient" in navigation
        patientButton.setStyle("-fx-background-color: #44A5FF; -fx-border-color: black;");
    }

    @FXML
    private void goToHomePage() throws IOException {
        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("Fxml/PatientHomePage.fxml"));
        Parent root = loader.load();
        PatientHomePageController patientHomePageController = loader.getController();
        patientHomePageController.setPatient(patient);
        Scene scene = new Scene(root);
        Stage stage = (Stage) patientButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToDoctorDetailsPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/DoctorDetailsPage.fxml"));
        Parent root = loader.load();
        DoctorDetailsPageController doctorDetailsPageController = loader.getController();
        doctorDetailsPageController.setDoctor(doctor, hospitalName.getText());
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Doctor details");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void editAge() {
        if (!age.isEditable()) {
            age.setEditable(true);
            ageImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            age.setEditable(false);
            ageImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update age in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET age=" + age.getText() + " WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setAge(Integer.parseInt(age.getText()));
        }
    }

    @FXML
    private void editAddress() {
        if (!address.isEditable()) {
            address.setEditable(true);
            addressImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            address.setEditable(false);
            addressImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            // to update address in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET address='" + address.getText() + "' WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setAddress(address.getText());
        }
    }

    @FXML
    private void editContact() {
        if (!contact.isEditable()) {
            contact.setEditable(true);
            contactImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            contact.setEditable(false);
            contactImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update contact in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET contact='" + contact.getText() + "' WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setContact(contact.getText());
        }
    }

    @FXML
    private void openMedicalRecord() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/MedicalRecordPage.fxml"));
        Parent root = loader.load();
        MedicalRecordPageController medicalRecordPageController = loader.getController();
        medicalRecordPageController.setPatient(OIB.getText());
        medicalRecordPageController.setFlag("patient");
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Medical record");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void logout() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Fxml/LoginPage.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}