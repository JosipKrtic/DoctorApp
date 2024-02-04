package doctor.app.doctorapp;

import doctor.app.doctorapp.Models.Doctor;
import doctor.app.doctorapp.Models.Patient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    private Button doctorLogin, patientLogin;

    @FXML
    private TextField doctorIDTextField, doctorPasswordTextField, OIBtextField, patientPasswordTextField;

    BorderStroke redBorder = new BorderStroke(
            Color.RED, BorderStrokeStyle.SOLID, null, null
    );

    BorderStroke noBorder = new BorderStroke(
            Color.rgb(217, 217, 217), BorderStrokeStyle.SOLID, null, null
    );


    Doctor activeDoctor;
    Patient activePatient;

    @FXML
    public void launchAppForDoctor() throws IOException {
        if (validationForLogin(doctorIDTextField) && validationForLogin(doctorPasswordTextField)) {
            setNewSceneForDoctor(doctorIDTextField, doctorPasswordTextField);
        }
    }

    @FXML
    public void launchAppForPatient() throws IOException {
        if (validationForLogin(OIBtextField) && validationForLogin(patientPasswordTextField)) {
            setNewSceneForPatient(OIBtextField, patientPasswordTextField);
        }
    }

    private boolean validationForLogin(TextField input) {
        if (input.getText().equals("")) {
            input.setBorder(new Border(redBorder));
            return false;
        } else {
            input.setBorder(new Border(noBorder));
            return true;
        }
    }

    private void setNewSceneForDoctor(TextField id, TextField password) throws IOException {
        //get all Doctors from DB and check if the input matches
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Doctor";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int ID = result.getInt("doctorID");
                String passwordDB = result.getString("password");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String contact = result.getString("contact");
                int hospitalID = result.getInt("IdHospital");
                if (String.valueOf(ID).equals(id.getText()) && passwordDB.equals(password.getText())) {
                    //creates Doctor object to send to Home Page controller
                    activeDoctor = new Doctor(ID, passwordDB, firstName, lastName, contact, hospitalID);
                    System.out.println(activeDoctor.getFirstName() + " " + activeDoctor.getLastName() + " logged in!");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/DoctorHomePage.fxml"));
                    Parent root = loader.load();
                    HomePageController homePageController = loader.getController();
                    homePageController.setDoctor(activeDoctor);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) doctorLogin.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    break;
                }  else {
                    id.setBorder(new Border(redBorder));
                    password.setBorder(new Border(redBorder));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setNewSceneForPatient(TextField patientOIB, TextField password) throws IOException {
        // calls procedure "desifriraj" to send OIB and password and to see if it matches with DB
        Connection connection = SQLServerConnection.getConnection();
        try {
            String callProcedure = "{call desifriraj(?, ?, ?)}";
            try (CallableStatement stmt = connection.prepareCall(callProcedure)) {
                stmt.setInt(1, Integer.parseInt(patientOIB.getText()));
                stmt.setString(2, password.getText());
                stmt.registerOutParameter(3, Types.BIT); // Output parameter for the result
                stmt.execute();

                boolean isPasswordMatch = stmt.getBoolean(3);

                // If the OIB and password matched a row in DB, select that patient
                if (isPasswordMatch) {
                    String sql = "SELECT * FROM Patient WHERE OIB=" + patientOIB.getText();
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(sql);
                    while (result.next()) {
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
                        int doctorID = result.getInt("doctorID");
                        // create Patient object and send it to Home Page controller
                        activePatient = new Patient(Integer.parseInt(patientOIB.getText()), password.getText(), firstName, lastName, age, sex, address, contact,
                                bloodType, weight, height, medicalRecordID, insuranceID, doctorID);
                        System.out.println(activePatient.getFirstName() + " " + activePatient.getLastName() + " logged in!");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Fxml/PatientHomePage.fxml"));
                        Parent root = loader.load();
                        PatientHomePageController patientHomePageController = loader.getController();
                        patientHomePageController.setPatient(activePatient);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) patientLogin.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                        break;
                    }
                } else {
                    patientOIB.setBorder(new Border(redBorder));
                    password.setBorder(new Border(redBorder));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}