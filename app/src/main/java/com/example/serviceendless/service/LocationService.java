package com.example.serviceendless.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.example.serviceendless.R;
import com.example.serviceendless.enums.Action;
import com.example.serviceendless.listener.LocationListener;
import com.example.serviceendless.utils.ServiceTracker;

public class LocationService extends Service {

    private PowerManager.WakeLock wakeLock = null;
    private boolean isServiceStarted = false;
    private ServiceTracker serviceTracker;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceTracker = new ServiceTracker();

        Notification notification = createNotification();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartIntent = new Intent(getApplicationContext(), LocationService.class);
        restartIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(this,1,restartIntent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //android.os.Debug.waitForDebugger();
        if(intent != null){
            String action = intent.getAction();
            switch (Action.valueOf(action)){
                case START:
                    startService();
                    break;
                case STOP:
                    stopService();
                    break;
                default:
                    break;
            }
            startService();
        }
        return START_STICKY;
    }

    private Notification createNotification(){
        String notChannelID = "ENDLESS SERVICE CHANNEL";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(notChannelID,"Endless Service notifications channel",NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Endless Service channel");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(channel);
        }
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new Notification.Builder(this,notChannelID);
        }
        else
            builder = new Notification.Builder(this);
        return builder.setContentTitle("Endless Service")
                .setContentText("This is your favorite endless service working")
                .setSmallIcon(R.mipmap.ic_logo)
                .setTicker("Ticker text")
                .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
                .build();
    }

    @SuppressLint({"MissingPermission", "WakelockTimeout"})
    private void startService(){
        if(isServiceStarted)
            return;
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show();
        isServiceStarted = true;
        serviceTracker.setServiceState(this, ServiceTracker.ServiceState.STARTED);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"wakeLock" + getPackageName());
        wakeLock.acquire();

        locationListener = new LocationListener();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60 * 1000,0, locationListener);
    }

    private void stopService(){
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show();
        try {
            if(wakeLock != null && wakeLock.isHeld())
               wakeLock.release();
            stopForeground(true);
            stopSelf();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        isServiceStarted = false;
        serviceTracker.setServiceState(this, ServiceTracker.ServiceState.STOPPED);
    }
}
