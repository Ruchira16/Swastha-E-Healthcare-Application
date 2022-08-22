package com.example.userregistration;

public class Book {

    String userName;
    String email;
    String dob;
    String Aptdate;
    String doctorName;
    String phoneNumber;
    String location;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    String gender;

    public Book() {

    }



    public Book(String userName, String email ,String dob,String gender, String aptdate, String doctorName, String phoneNumber, String location) {
        this.userName = userName;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.Aptdate = aptdate;
        this.doctorName = doctorName;
        this.phoneNumber = phoneNumber;
        this.location = location;


    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAptdate() {
        return Aptdate;
    }

    public void setAptdate(String aptdate) {
        Aptdate = aptdate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
