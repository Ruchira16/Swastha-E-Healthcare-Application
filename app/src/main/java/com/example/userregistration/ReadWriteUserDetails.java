package com.example.userregistration;

public class ReadWriteUserDetails {

    public ReadWriteUserDetails(){

    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String fullName;
    public String dob;
    public String email;
    public String gender;

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String profileImg;

    public ReadWriteUserDetails(String name, String dob, String email, String gender){
        this.fullName = name;
        this.dob = dob;
        this.email = email;
        this.gender = gender;
        this.profileImg = profileImg;

    }
}
