package com.example.serviceendless;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.serviceendless.enums.Action;
import com.example.serviceendless.service.LocationService;
import com.example.serviceendless.utils.ServiceTracker;

public class MainActivity extends AppCompatActivity {

    private ServiceTracker serviceTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceTracker = new ServiceTracker();

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionOnService(Action.START);
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionOnService(Action.STOP);
            }
        });
    }

    private void actionOnService(Action action){
        if(serviceTracker.getServiceState(this) == ServiceTracker.ServiceState.STOPPED && action == Action.STOP){
            return;
        }
        Intent intent = new Intent(this, LocationService.class);
        intent.setAction(action.name());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(intent);
        else
            startService(intent);

    }
}