package com.example.secondapplication.Entities;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customer_table")
public class Customer {
    @PrimaryKey
    @NonNull
    private String id;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String street;
    private int buildingNumber;
    private int postalAddress;
    private String phoneNumber;
    private String email;
    private String password;

    public Customer() {
    }

    public Customer(@NonNull String id, String firstName, String lastName, String city, String country, String street, int buildingNumber, int postalAddress, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.postalAddress = postalAddress;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //---------Methods-------------//
    @Exclude
    public String getAddress(){
        return street+" "+buildingNumber+","+city+" "+country;
    }

    //----------Get&Set-------------//

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public int getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(int postalAddress) {
        this.postalAddress = postalAddress;
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
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                ", buildingNumber=" + buildingNumber +
                ", postalAddress=" + postalAddress +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
