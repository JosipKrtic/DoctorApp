package doctor.app.doctorapp.Models;

import doctor.app.doctorapp.SQLServerConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Appointment {

    private int ID;
    private Date date;
    private String time;
    private int duration;
    private String symptoms;
    private int patientOIB;
    private int doctorID;

    public Appointment(int ID, Date date, String time, int duration, String symptoms, int patientOIB, int doctorID) {
        this.ID = ID;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.symptoms = symptoms;
        this.patientOIB = patientOIB;
        this.doctorID = doctorID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public int getPatientOIB() {
        return patientOIB;
    }

    public void setPatientOIB(int patientOIB) {
        this.patientOIB = patientOIB;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public List<String> getAppointmentRow() {
        String patientName = "";
        //set the hospital name
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Patient";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                int OIB = result.getInt("OIB");
                if (OIB == this.patientOIB) {
                    String firstName = result.getString("firstName");
                    String lastName = result.getString("lastName");
                    patientName = firstName + " " + lastName;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Arrays.asList(patientName, this.date.toString(), this.time);
    }

    public List<String> getAppointmentRowForPatient() {
        return Arrays.asList(this.date.toString(), this.time, this.symptoms);
    }

    //to return patient name, date, time, duration and symptoms...
    public List<String> getAppointmentRowForAppointmentListView() {
        String patientName = "";
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Patient WHERE OIB=" + this.patientOIB;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                patientName = firstName + " " + lastName;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Arrays.asList(patientName, this.date.toString(), this.time, String.valueOf(this.duration), this.symptoms);
    }

    public String getPatientNameFromAppointment() {
        String patientName = "";
        Connection connection = SQLServerConnection.getConnection();
        try {
            String sql = "SELECT * FROM Patient WHERE OIB=" + this.patientOIB;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                patientName = firstName + " " + lastName;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return patientName;
    }
}
