package doctor.app.doctorapp.Models;

import java.util.Arrays;
import java.util.List;

public class Patient {
    private int OIB;
    private String password;
    private String firstName;
    private String lastName;
    private int age;
    private String sex;
    private String address;
    private String contact;
    private String bloodType;
    private int height;
    private int weight;
    private int medicalRecordID;
    private int insuranceID;
    private int doctorID;

    public Patient(int OIB, String password, String firstName, String lastName, int age, String sex, String address,
                   String contact, String bloodType, int height, int weight, int medicalRecordID, int insuranceID, int doctorID) {
        this.OIB = OIB;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.contact = contact;
        this.bloodType = bloodType;
        this.height = height;
        this.weight = weight;
        this.medicalRecordID = medicalRecordID;
        this.insuranceID = insuranceID;
        this.doctorID = doctorID;
    }

    public int getOIB() {
        return OIB;
    }

    public void setOIB(int OIB) {
        this.OIB = OIB;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBloodType() {
        return bloodType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getInsuranceID() {
        return insuranceID;
    }

    public void setInsuranceID(int insuranceID) {
        this.insuranceID = insuranceID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public List<String> getPatientRow() {
        return Arrays.asList(this.firstName + " " + this.lastName, String.valueOf(this.age), this.bloodType);
    }

    public List<String> getPatientRowForPatientListView() {
        return Arrays.asList(String.valueOf(this.OIB), this.firstName, this.lastName, String.valueOf(this.age),
                this.sex, this.bloodType, this.contact, String.valueOf(this.medicalRecordID));
    }
}
