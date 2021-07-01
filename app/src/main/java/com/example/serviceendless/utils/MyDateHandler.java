package com.example.serviceendless.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateHandler {

    public static String getDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh_mm_ss");
        return dateFormat.format(date);
    }
}
