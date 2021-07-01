package com.example.serviceendless.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class ServiceTracker {
    public enum ServiceState {
        STARTED,
        STOPPED,
    }

    public ServiceTracker(){}

    private static final String name = "SPYSERVICE_KEY";
    private static final String key = "SPYSERVICE_STATE";

    public void setServiceState(Context context, ServiceState state){
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putString(key,state.name()).apply();
    }

    public ServiceState getServiceState(Context context){
        SharedPreferences sharedPreferences = getPreferences(context);
        String value = sharedPreferences.getString(key, ServiceState.STOPPED.name());
        return ServiceState.valueOf(value);
    }

    private SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
