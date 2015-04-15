package com.lingoware.lingow.alarma;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class SchedulingService extends IntentService {
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "Scheduling Demo";
    private static final String SEARCH_STRING = "doodle";
    private NotificationManager mNotificationManager;

    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String urlString = "http://www.google.com";
            String result="";
            
            try{
                result = loadFromNetwork(urlString);
            } catch (IOException e) {
                Log.i(TAG, getString(R.string.connection_error));
            }
            if(result.contains((SEARCH_STRING))){
                sendNotification(getString(R.string.doodle_found));
            } else {
                sendNotification(getString(R.string.no_doodle));
            }
        }
    }

    private InputStream downloadUrl(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    private String readIt(InputStream stream) throws IOException{
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine();line!= null ; line = reader.readLine()){
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    private String loadFromNetwork(String urlString) throws IOException{
        InputStream stream = null;
        String str = "";
        try{
            stream = downloadUrl(urlString);
            str = readIt(stream);
        }finally {
            if(stream != null){stream.close();}
        }
        return str;
    }

    private void sendNotification(String msg){
        mNotificationManager =(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this,0,new Intent(this,ActivityMain.class),0);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.doodle_alert))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);
        
        nb.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID,nb.build());

    }
}
