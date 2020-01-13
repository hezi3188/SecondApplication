package com.example.secondapplication.Entities;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "customer_table")
public class Customer {
    @PrimaryKey
    @NonNull
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private double Latitude;
    private double Longitude;
    private String phoneNumber;
    private String email;
    private String password;

    public Customer() {
    }

    @Ignore
    public Customer(@NonNull String id, String firstName, String lastName, String address, double latitude, double longitude, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        Latitude = latitude;
        Longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //---------Methods-------------//

    public String getAddress(){
        return address;
    }

    //----------Get&Set-------------//


    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
