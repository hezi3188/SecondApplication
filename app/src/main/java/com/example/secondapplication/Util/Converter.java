package com.example.secondapplication.Util;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.secondapplication.Entities.ParcelStatus;
import com.example.secondapplication.Entities.ParcelType;
import com.example.secondapplication.Entities.ParcelWeight;

import java.util.Date;


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
    public String fromParcelStatusToString(ParcelStatus parcelStatus){
        switch (parcelStatus) {
            case SENT:
                return "SENT";
            case IN_COLLECTION_PROCESS:
                return "IN_COLLECTION_PROCESS";
            case ON_THE_WAY:
                return "ON_THE_WAY";
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
            case "IN_COLLECTION_PROCESS":
                return ParcelStatus.IN_COLLECTION_PROCESS;
            case "ON_THE_WAY":
                return ParcelStatus.ON_THE_WAY;
            case "ACCEPTED":
                return ParcelStatus.ACCEPTED;
        }
        return null;
    }

    @TypeConverter
    public String fromParcelTypeToString(ParcelType parcelType){
        switch (parcelType) {
            case ENVELOPE:
                return "ENVELOPE";
            case SMALL_PACKAGE:
                return "SMALL_PACKAGE";
            case BIG_PACKAGE:
                return "BIG_PACKAGE";
        }
        return null;
    }

    @TypeConverter
    public ParcelType fromStringToParcelType(String s){
        switch (s){
            case "ENVELOPE":
                return ParcelType.ENVELOPE;
            case "SMALL_PACKAGE":
                return ParcelType.SMALL_PACKAGE;
            case "BIG_PACKAGE":
                return ParcelType.BIG_PACKAGE;
        }
        return null;
    }

    @TypeConverter
    public String fromParcelWeightToString(ParcelWeight parcelWeight){
        switch (parcelWeight) {
            case UNTIL_500_GR:
                return "UNTIL_500_GR";
            case UNTIL_1_KG:
                return "UNTIL_1_KG";
            case UNTIL_5_KG:
                return "UNTIL_5_KG";
            case UNTIL_20_KG:
                return "UNTIL_20_KG";
        }
        return null;
    }

    @TypeConverter
    public ParcelWeight fromStringToParcelWeight(String s){
        switch (s){
            case "UNTIL_500_GR":
                return ParcelWeight.UNTIL_500_GR;
            case "UNTIL_1_KG":
                return ParcelWeight.UNTIL_1_KG;
            case "UNTIL_5_KG":
                return ParcelWeight.UNTIL_5_KG;
            case "UNTIL_20_KG":
                return ParcelWeight.UNTIL_20_KG;
        }
        return null;
    }
}
