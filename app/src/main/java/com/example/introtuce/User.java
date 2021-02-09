package com.example.introtuce;

import java.util.Date;
import android.net.Uri;
public class User {
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private String country;
    private String state;
    private String homeTown;
    private long phoneNumber;
    private long timestamp;
    private String imageUri;
    public User(){

    }
    public User(String[] s){
        this(s[0],s[1],s[2],s[3],s[4],s[5],s[6],Long.parseLong(s[7]),Long.parseLong(s[8]),s[9]);
    }
    public User(String firstName, String lastName, String dob, String gender, String country, String state, String homeTown, long phoneNumber,long timestamp,String imageUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.country = country;
        this.state = state;
        this.homeTown = homeTown;
        this.phoneNumber = phoneNumber;
        this.timestamp=timestamp;
        this.imageUri=imageUri;
    }

    public String getFirstName() {
        return firstName;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
