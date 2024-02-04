package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Appointment;
import doctor.app.doctorapp.Models.Doctor;
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

public class AppointmentListPageController implements Initializable {

    @FXML
    private Text hospitalName;

    @FXML
    private TableView<ObservableList<String>> appointmentTable;
    @FXML
    private TableColumn<ObservableList<String>, String> patientColumn, dateColumn, timeColumn, durationColumn, symptomsColumn;

    @FXML
    private Button homeButton, patientsButton, appointmentsButton, accountButton;

    private Doctor doctor;

    List<Appointment> appointmentList = new ArrayList<>();


    public void setDoctor(Doctor doctor, String hospital) {
        this.doctor = doctor;

        //set hospital name
        hospitalName.setText(hospital);

        //to add appointments to the list of appointments that are connected to this doctor
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Appointment WHERE doctorID=" + doctor.getDoctorID();
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
                appointmentList.add(new Appointment(ID, date, time, duration, symptoms, patientOIB, doctor.getDoctorID()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //to add appointments to the table view
        ObservableList<ObservableList<String>> appointmentData = FXCollections.observableArrayList();
        for (Appointment appointment : appointmentList) {
            appointmentData.add(FXCollections.observableArrayList(appointment.getAppointmentRowForAppointmentListView()));
        }

        appointmentTable.setItems(appointmentData);

        patientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        symptomsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Highlight "Appointments" in navigation
        appointmentsButton.setStyle("-fx-background-color: #44A5FF; -fx-border-color: black;");
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
    private void createAppointmentPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AddAppointmentPage.fxml"));
        Parent root = loader.load();
        AddAppointmentPageController addAppointmentPageController = loader.getController();
        addAppointmentPageController.setDoctor(doctor);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("New appointment");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void openAppointmentDetails() {
        appointmentTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) { // Check for double click
                List<String> selectedItem = appointmentTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String patientName = selectedItem.get(0);
                    String date = selectedItem.get(1);
                    String time = selectedItem.get(2);
                    for (Appointment appointment : appointmentList) {
                        String patientFullName = appointment.getPatientNameFromAppointment();
                        if (patientFullName.equals(patientName) && appointment.getDate().toString().equals(date) && appointment.getTime().equals(time)) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AppointmentDetailsPage.fxml"));
                            Parent root;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            AppointmentDetailsPageController appointmentDetailsPageController = loader.getController();
                            appointmentDetailsPageController.setAppointment(appointment);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.setTitle("Appointment details");
                            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
                            stage.show();
                            break;
                        }
                    }
                }
            }
        });
    }
}