package com.example.serviceendless.listener;

import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;

import com.example.serviceendless.utils.MyDateHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

public class LocationListener implements android.location.LocationListener {

    private File myFile;
    private OutputStreamWriter myOutWriter;
    private FileOutputStream fOut = null;

    public LocationListener(){
        final String FILES = "/ServiceEndless/Logs";
//aa
        String path = Environment.getExternalStorageDirectory().getPath() + FILES;

        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        myFile = new File(folderFile, MyDateHandler.getDate(new Date()) +".txt");

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        System.out.println("onLocationChanged");
        try {
            fOut = new FileOutputStream(myFile,true);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(MyDateHandler.getDate(new Date())).append(" NEW LOCATION ").append(String.valueOf(location.getLatitude())).append(",")
                    .append(String.valueOf(location.getLongitude())).append("\n");
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        try {
            fOut = new FileOutputStream(myFile,true);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(MyDateHandler.getDate(new Date())).append(" onStatusChanged ").append(provider).append(" ")
                    .append(String.valueOf(status)).append("\n");
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        try {
            fOut = new FileOutputStream(myFile,true);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(MyDateHandler.getDate(new Date())).append(" onProviderEnabled ").append(provider).append("\n");

            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        try {
            fOut = new FileOutputStream(myFile,true);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(MyDateHandler.getDate(new Date())).append(" onProviderDisabled ").append(provider).append("\n");
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
