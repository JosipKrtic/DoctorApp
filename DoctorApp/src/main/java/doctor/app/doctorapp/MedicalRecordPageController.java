package doctor.app.doctorapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordPageController {

    @FXML
    private Text patient;

    @FXML
    private CheckBox pastIllnesses1, pastIllnesses2, pastIllnesses3, pastIllnesses4, pastIllnesses5, pastIllnesses6,
            pastIllnesses7, pastIllnesses8, pastIllnesses9, pastIllnesses10, chronicConditions1, chronicConditions2,
            chronicConditions3, chronicConditions4, chronicConditions5, chronicConditions6, chronicConditions7,
            chronicConditions8, chronicConditions9, chronicConditions10, surgeries1, surgeries2, surgeries3, surgeries4,
            surgeries5, surgeries6, surgeries7, surgeries8, surgeries9, surgeries10, familyHistory1, familyHistory2,
            familyHistory3, familyHistory4, familyHistory5, familyHistory6, familyHistory7, familyHistory8, familyHistory9,
            familyHistory10;


    private int medicalRecordID;

    public void setPatient(String patientOIB) {
        medicalRecordID = 0;
        String conditions = "";
        //get the patient with provided OIB
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Patient WHERE OIB=" + patientOIB;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String patientFirstName = result.getString("firstName");
                String patientLastName = result.getString("lastName");
                String patientName = patientFirstName + " " + patientLastName;
                patient.setText(patientName + "'s medical record");
                medicalRecordID = result.getInt("medicalRecordID");
            }

            //get medical record associated with the current patient
            sql = "SELECT * FROM MedicalRecord WHERE ID=" + medicalRecordID;
            statement = connection.createStatement();
            result = statement.executeQuery(sql);
            while (result.next()) {
                conditions = result.getString("conditions");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        setConditionsForPatient(conditions);
    }

    public void setFlag(String flag) {
        //if the patient is viewing the medical record page, all the fields are set to disabled
        if (flag.equals("patient")) {
            List<CheckBox> checkboxes = getAllCheckboxes();
            for (CheckBox checkbox : checkboxes) {
                checkbox.setDisable(true);
            }
        }
    }

    private void setConditionsForPatient(String conditions) {
        List<Integer> conditionsList = new ArrayList<>();
        //for each char (0 or 1) from the String object create a list of 40 Integers
        for (char c : conditions.toCharArray()) {
            int condition = Character.getNumericValue(c);
            conditionsList.add(condition);
        }

        //Creates a list of checkboxes
        List<CheckBox> checkboxes = getAllCheckboxes();

        //set each checkbox to selected (if the value is 1) or not selected (if the value is 0)
        for (int i = 0; i < conditionsList.size() && i < checkboxes.size(); i++) {
            CheckBox checkbox = checkboxes.get(i);
            int value = conditionsList.get(i);
            checkbox.setSelected(value == 1);
        }
    }

    private List<CheckBox> getAllCheckboxes() {
        // Add all your checkboxes to this list in the correct order
        return List.of(pastIllnesses1, pastIllnesses2, pastIllnesses3, pastIllnesses4, pastIllnesses5, pastIllnesses6,
                pastIllnesses7, pastIllnesses8, pastIllnesses9, pastIllnesses10, chronicConditions1, chronicConditions2,
                chronicConditions3, chronicConditions4, chronicConditions5, chronicConditions6, chronicConditions7,
                chronicConditions8, chronicConditions9, chronicConditions10, surgeries1, surgeries2, surgeries3, surgeries4,
                surgeries5, surgeries6, surgeries7, surgeries8, surgeries9, surgeries10, familyHistory1, familyHistory2,
                familyHistory3, familyHistory4, familyHistory5, familyHistory6, familyHistory7, familyHistory8, familyHistory9,
                familyHistory10);
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        List<CheckBox> checkboxes = getAllCheckboxes();
        StringBuilder conditions = new StringBuilder();
        for (CheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                conditions.append("1");
            } else {
                conditions.append("0");
            }
        }

        //updates the medical record with new changes made
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "UPDATE MedicalRecord SET conditions='" + conditions + "' WHERE ID=" + medicalRecordID;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}