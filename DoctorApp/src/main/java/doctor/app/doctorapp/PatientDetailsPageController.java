package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import doctor.app.doctorapp.Models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class PatientDetailsPageController {

    @FXML
    private Text patientText;

    @FXML
    private TextField OIB, password, firstName, lastName, age, sex, address, contact, bloodType, height, weight, insurance;

    @FXML
    private ImageView lastNameImage, ageImage, contactImage, addressImage, weightImage, heightImage, insuranceImage;

    private Doctor doctor;
    private Patient patient;

    BorderStroke redBorder = new BorderStroke(
            Color.RED, BorderStrokeStyle.SOLID, null, null
    );
    BorderStroke noBorder = new BorderStroke(
            Color.rgb(217, 217, 217), BorderStrokeStyle.SOLID, null, null
    );

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        //set patient name at the top
        patientText.setText(patient.getFirstName() + " " + patient.getLastName());

        //set all patient info
        OIB.setText(String.valueOf(patient.getOIB()));
        password.setText(/*patient.getPassword()*/"************");
        firstName.setText(patient.getFirstName());
        lastName.setText(patient.getLastName());
        age.setText(String.valueOf(patient.getAge()));
        sex.setText(patient.getSex());
        address.setText(patient.getAddress());
        contact.setText(patient.getContact());
        bloodType.setText(patient.getBloodType());
        height.setText(String.valueOf(patient.getHeight()));
        weight.setText(String.valueOf(patient.getWeight()));

        //get the insurance name/type from Insurance table by insuranceID (foreign key) from Patient table
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
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void editLastName() {
        if (!lastName.isEditable()) {
            lastName.setEditable(true);
            lastNameImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            lastName.setEditable(false);
            lastNameImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update lastName in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET lastName='" + lastName.getText() + "' WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setLastName(lastName.getText());
        }
    }

    @FXML
    private void editAge() {
        if (!age.isEditable()) {
            age.setEditable(true);
            ageImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            age.setEditable(false);
            ageImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to edit the age in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET age=" + age.getText() + " WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                age.setBorder(new Border(noBorder));
            } catch (SQLException ex) {
                //catch exception if the age is invalid (<0) defined by the constraint in DB
                if (ex.getMessage().contains("The UPDATE statement conflicted with the CHECK constraint " +
                        "\"Positive_Patient_Age\". The conflict occurred in database \"DoctorApp\", table " +
                        "\"dbo.Patient\", column 'age'")) {
                    System.out.println("The age on patient can't be negative value!");
                    age.setBorder(new Border(redBorder));
                    age.setEditable(true);
                    ageImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
                } else {
                    ex.printStackTrace();
                }
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
            //to update address in DB
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
    private void editHeight() {
        if (!height.isEditable()) {
            height.setEditable(true);
            heightImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            height.setEditable(false);
            heightImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update height in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET height=" + height.getText() + " WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setHeight(Integer.parseInt(height.getText()));
        }
    }

    @FXML
    private void editWeight() {
        if (!weight.isEditable()) {
            weight.setEditable(true);
            weightImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            weight.setEditable(false);
            weightImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to update weight in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET weight=" + weight.getText() + " WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setWeight(Integer.parseInt(weight.getText()));
        }
    }

    @FXML
    private void editInsurance() {
        if (!insurance.isEditable()) {
            insurance.setEditable(true);
            insuranceImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/check.png"))));
        } else {
            insurance.setEditable(false);
            insuranceImage.setImage(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/edit.png"))));
            //to edit insuranceID in DB
            Connection connection = SQLServerConnection.getConnection();
            try {
                String sql = "UPDATE Patient SET insuranceID=" + insurance.getText() + " WHERE OIB=" + patient.getOIB();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            patient.setInsuranceID(Integer.parseInt(insurance.getText()));

            //to change name of insurance in patient details list by new insuranceID
            String typeOfInsurance = "";
            connection = SQLServerConnection.getConnection();
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
        }
    }

    @FXML
    private void createAppointmentPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/AddAppointmentPage.fxml"));
        Parent root = loader.load();
        AddAppointmentPageController addAppointmentPageController = loader.getController();
        addAppointmentPageController.setDoctor(doctor);
        addAppointmentPageController.setPatientOIB(OIB.getText());
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("New appointment");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void openPrescriptions() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/PrescriptionPage.fxml"));
        Parent root = loader.load();
        PrescriptionPageController prescriptionPageController = loader.getController();
        prescriptionPageController.setDoctor(doctor);
        prescriptionPageController.setPatient(patient);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Prescriptions");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void openMedicalRecord() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/MedicalRecordPage.fxml"));
        Parent root = loader.load();
        MedicalRecordPageController medicalRecordPageController = loader.getController();
        medicalRecordPageController.setPatient(OIB.getText());
        medicalRecordPageController.setFlag("doctor");
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Medical record");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("Images/logo.png"))));
        stage.show();
    }

    @FXML
    private void deletePatient(ActionEvent event) {
        //to delete the patient
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "DELETE FROM Patient WHERE OIB=" + patient.getOIB();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeWindow(event);
    }
}