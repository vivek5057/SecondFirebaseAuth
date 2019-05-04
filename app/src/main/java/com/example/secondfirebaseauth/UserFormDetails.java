package com.example.secondfirebaseauth;

public class UserFormDetails {

    public String fullName;
    public String userName;
    public String currentCourse;
    public String collegeName;
    public String mobileNo;

    public UserFormDetails(){}

    public UserFormDetails(String userName,String fullName, String currentCourse, String collegeName, String mobileNo) {
        this.userName = userName;
        this.fullName = fullName;
        this.currentCourse = currentCourse;
        this.collegeName = collegeName;
        this.mobileNo = mobileNo;
    }
}
