package com.example.secondapplication.Util;

import androidx.room.TypeConverter;

import com.example.secondapplication.Entities.ParcelStatus;
import com.example.secondapplication.Entities.ParcelType;
import com.example.secondapplication.Entities.ParcelWeight;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public  class Converter{

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public static String fromParcelStatusToString(ParcelStatus parcelStatus){
        switch (parcelStatus) {
            case SENT:
                return "SENT";
            case IN_COLLECTION_PROCESS:
                return "OFFERED";
            case ON_THE_WAY:
                return "ON THE WAY";
            case ACCEPTED:
                return "ACCEPTED";
        }
        return null;
    }

    @TypeConverter
    public ParcelStatus fromStringToParcelStatus(String s){
        switch (s){
            case "SENT":
                return ParcelStatus.SENT;
            case "OFFERED":
                return ParcelStatus.IN_COLLECTION_PROCESS;
            case "ON THE WAY":
                return ParcelStatus.ON_THE_WAY;
            case "ACCEPTED":
                return ParcelStatus.ACCEPTED;
        }
        return null;
    }

    @TypeConverter
    public static String fromParcelTypeToString(ParcelType parcelType){
        switch (parcelType) {
            case ENVELOPE:
                return "envelop";
            case SMALL_PACKAGE:
                return "small package";
            case BIG_PACKAGE:
                return "big package";
        }
        return null;
    }

    @TypeConverter
    public ParcelType fromStringToParcelType(String s){
        switch (s){
            case "envelop":
                return ParcelType.ENVELOPE;
            case "small package":
                return ParcelType.SMALL_PACKAGE;
            case "big package":
                return ParcelType.BIG_PACKAGE;
        }
        return null;
    }

    @TypeConverter
    public static String fromParcelWeightToString(ParcelWeight parcelWeight){
        switch (parcelWeight) {
            case UNTIL_500_GR:
                return "until 500 gr";
            case UNTIL_1_KG:
                return "until 1 kg";
            case UNTIL_5_KG:
                return "until 5 kg";
            case UNTIL_20_KG:
                return "until 20 kg";
        }
        return null;
    }

    @TypeConverter
    public ParcelWeight fromStringToParcelWeight(String s){
        switch (s){
            case "until 500 gr":
                return ParcelWeight.UNTIL_500_GR;
            case "until 1 kg":
                return ParcelWeight.UNTIL_1_KG;
            case "until 5 kg":
                return ParcelWeight.UNTIL_5_KG;
            case "until 20 kg":
                return ParcelWeight.UNTIL_20_KG;
        }
        return null;
    }

    @TypeConverter
    public static   String fromMapToString(Map<String,Boolean> messengers){
        if(messengers.size()==0)
            return "";
        String value="";
        for (Map.Entry<String,Boolean>entry:messengers.entrySet()) {
            String convert="("+entry.getKey()+","+entry.getValue()+")|";
            value=value+convert;
        }
        value=value.substring(0,value.length()-1);
        return value;
    }

    @TypeConverter
    public static Map<String,Boolean> fromStringToMap(String messengersString){
        if(messengersString.equals(""))
            return null;
        String[] messengersList=messengersString.split("\\|");
        Map<String,Boolean> messengers=new HashMap<>();
        for (String str:messengersList) {
            String []temp=str.substring(1,str.length()-1).split(",");

            messengers.put(temp[0],Boolean.parseBoolean(temp[1]));
        }
        return messengers;
    }

}
