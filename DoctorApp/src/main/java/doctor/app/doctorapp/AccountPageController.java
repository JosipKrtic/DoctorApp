package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccountPageController implements Initializable {

    @FXML
    private Text hospitalName;

    @FXML
    private Button homeButton, patientsButton, appointmentsButton, accountButton, signOutButton;

    @FXML
    private TextField ID, password, firstName, lastNameText, contactText, hospital, numOfPatients, numOfRemainingPatients, numOfAppointments;

    @FXML
    private ImageView passwordImage, lastNameImage, contactImage;

    private Doctor doctor;


    public void setDoctor(Doctor doctor, String nameHospital) {
        this.doctor = doctor;

        //set hospital name
        hospitalName.setText(nameHospital);

        ID.setText(String.valueOf(doctor.getDoctorID()));
        password.setText(doctor.getPassword());
        firstName.setText(doctor.getFirstName());
        lastNameText.setText(doctor.getLastName());
        contactText.setText(doctor.getContact());

        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Hospital";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("ID");
                String name = result.getString("name");
                if (this.doctor.getHospitalID() == ID) {
                    hospital.setText(name);
                }
            }

            //stored procedure to get total number of patients for current doctor
            String callProcedure = "{call GetNumOfPatients(?, ?, ?)}";
            try (CallableStatement stmt = connection.prepareCall(callProcedure)) {
                stmt.setInt(1, doctor.getDoctorID());
                stmt.registerOutParameter(2, Types.INTEGER);
                stmt.registerOutParameter(3, Types.INTEGER);

                stmt.execute();

                numOfPatients.setText(String.valueOf(stmt.getInt(2)));
                numOfRemainingPatients.setText(String.valueOf(stmt.getInt(3)));
            }

            //to get total number of appointment for current doctor
            sql = "SELECT COUNT(DISTINCT ID) FROM Appointment WHERE doctorID = " + doctor.getDoctorID();
            statement = connection.createStatement();
            result = statement.executeQuery(sql);
            if (result.next()) {
                numOfAppointments.setText(String.valueOf(result.getInt(1)));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Highlight "Account" in navigation
        accountButton.setStyle("-fx-background-color: #44A5FF; -fx-border-color: black;");
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
    private void editPassword() {
        if (!password.isEditable()) {
            password.setEditable(true);
            passwordImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            password.setEditable(false);
            passwordImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //To update password in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Doctor SET password=" + password.getText() + " WHERE doctorID=" + doctor.getDoctorID();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            doctor.setPassword(password.getText());
        }
    }

    @FXML
    private void editLastName() {
        if (!lastNameText.isEditable()) {
            lastNameText.setEditable(true);
            lastNameImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            lastNameText.setEditable(false);
            lastNameImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update lastName in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Doctor SET lastName='" + lastNameText.getText() + "' WHERE doctorID=" + doctor.getDoctorID();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            doctor.setLastName(lastNameText.getText());
        }
    }

    @FXML
    private void editContact() {
        if (!contactText.isEditable()) {
            contactText.setEditable(true);
            contactImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            contactText.setEditable(false);
            contactImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update contact in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Doctor SET contact=" + contactText.getText() + " WHERE doctorID=" + doctor.getDoctorID();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            doctor.setContact(contactText.getText());
        }
    }

    @FXML
    private void signOut() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Fxml/LoginPage.fxml")));
        Scene scene = new Scene(root);
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}