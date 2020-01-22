package com.example.secondapplication.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class Utils  {
    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
    public static double distanceBetweenTwoLocations(double latitudeA,double longitudeA,double latitudeB,double longitudeB){
        Location locationA = new Location("point A");
        locationA.setLatitude(latitudeA);
        locationA.setLongitude(longitudeA);
        Location locationB = new Location("point B");
        locationB.setLatitude(latitudeB);
        locationB.setLongitude(longitudeB);
        double distance = locationA.distanceTo(locationB) ;
        return distance/1000.0;

    }

    public static boolean realLocation(Geocoder coder,String strAddress){
        List<Address> address;
        Address location=null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address.size()==0) {
                return false;
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    public static Address getLocationFromAddress(Geocoder coder,String strAddress) {

        List<Address> address;
        Address location=null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

             location = address.get(0);

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return location;
    }


}
