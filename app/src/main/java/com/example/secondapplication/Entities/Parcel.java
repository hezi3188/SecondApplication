package com.example.secondapplication.Entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.secondapplication.Util.Converter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "parcel_table")
public class Parcel {
    @PrimaryKey
    @NonNull
    private String  parcelID;
    private ParcelType parcelType;
    private boolean isFragile;
    private ParcelWeight parcelWeight;
    private double Latitude;
    private double Longitude;
    private Date deliveryParcelDate;
    private Date getParcelDate;
    private ParcelStatus status;
    private String deliveryName;
    private String customerId;
    private String address;
    private Map<String, Boolean> messengers;
    private String phoneNumber;



    //-------------Ctors--------------------//
    public Parcel() {
        messengers =new HashMap<>();
        this.status = ParcelStatus.SENT;
        this.deliveryName = "NO";
        this.getParcelDate = new Date(System.currentTimeMillis());
    }



//-------------------------------------//


    //--------------Ges&Set-----------------//


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Boolean> getMessengers() {
        return messengers;
    }

    public void setMessengers(Map<String, Boolean> messengers) {
        this.messengers = messengers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public String getParcelID() {
        return parcelID;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }

    public ParcelWeight getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(ParcelWeight parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        this.Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public Date getDeliveryParcelDate() {
        return deliveryParcelDate;
    }

    public void setDeliveryParcelDate(Date deliveryParcelDate) {
        this.deliveryParcelDate = deliveryParcelDate;
    }

    public Date getGetParcelDate() {
        return getParcelDate;
    }

    public void setGetParcelDate(Date getParcelDate) {
        this.getParcelDate = getParcelDate;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelStatus status) {
        this.status = status;
    }

    //------------------------------------------//

    public void putMessenger(String deliveryName,Boolean status){
        messengers.put(deliveryName,status);

    }

    @Override
    public String toString() {
        return "Parcel{" +
                "parcelID='" + parcelID + '\'' +
                ", parcelType=" + parcelType +
                ", isFragile=" + isFragile +
                ", parcelWeight=" + parcelWeight +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", deliveryParcelDate=" + deliveryParcelDate +
                ", getParcelDate=" + getParcelDate +
                ", status=" + status +
                ", deliveryName='" + deliveryName + '\'' +
                ", customerId='" + customerId + '\'' +
                ", address='" + address + '\'' +
                ", messengers=" + messengers +
                '}';
    }
}
