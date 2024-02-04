package doctor.app.doctorapp.Models;

public class Doctor {

    private int doctorID;
    private String password;
    private String firstName;
    private String lastName;
    private String contact;
    private int hospitalID;

    public Doctor(int doctorID, String password, String firstName, String lastName, String contact, int hospitalID) {
        this.doctorID = doctorID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.hospitalID = hospitalID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getHospitalID() {
        return hospitalID;
    }
}
