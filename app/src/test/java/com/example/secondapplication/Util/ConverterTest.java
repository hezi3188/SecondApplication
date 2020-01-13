package com.example.secondapplication.Util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ConverterTest {

    @Test
    public void fromMapToString() {
        Map<String,Boolean> hashMap=new HashMap<>();
        hashMap.put("hezi3188@gmail.com",false);
        hashMap.put("s2321111@gmail.com",false);
        hashMap.put("work@gmail.com",true);
        String string="(hezi3188@gmail.com,false)|(s2321111@gmail.com,false)|(work@gmail.com,true)";
        String expected=Converter.fromMapToString(hashMap);
        assertEquals(expected, string);

    }

    @Test
    public void fromStringToMap() {
        String string="(hezi3188@gmail.com,false)|(s2321111@gmail.com,false)|(work@gmail.com,true)";
        Map<String,Boolean> map=new HashMap<>();
        map=Converter.fromStringToMap(string);

        Map<String,Boolean> hashMap=new HashMap<>();
        hashMap.put("hezi3188@gmail.com",false);
        hashMap.put("s2321111@gmail.com",false);
        hashMap.put("work@gmail.com",true);

        assertEquals(hashMap,map);


    }
}