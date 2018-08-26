package com.jinojino.happytogether.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jinojino.happytogether.R;
import com.jinojino.happytogether.activity.MainActivity;
import com.jinojino.happytogether.http.ServiceThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetNoiseService extends Service {
    NotificationManager Notifi_M;
    ServiceThread thread;
    Notification Notifi ;

    public GetNoiseService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            // The id of the channel.
            String idd= "my_channel_01";

            // The user-visible name of the channel.
            CharSequence name = getString(R.string.app_name);

            // The user-visible description of the channel.
            String description = "ddd";

            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel(idd, name,importance);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            Notifi_M.createNotificationChannel(mChannel);

            Intent intent = new Intent(GetNoiseService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(GetNoiseService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            int count;
            boolean flag = false;
            String roomName = "";
            try{
                JSONArray jarray = new JSONArray(thread.getResult());

                JSONArray countArray = jarray.getJSONArray(0);
                JSONObject countObject = countArray.getJSONObject(0);
                count = countObject.getInt("count");
                for(int i=0; i<count; i++){
                }

                for(int j=1; j<=count; j++){
                    JSONArray roomArray = jarray.getJSONArray(j);
                    for(int i=0; i < roomArray.length(); i++){
                        JSONObject noiseObject = roomArray.getJSONObject(i);  // JSONObject 추출
                        int id = noiseObject.getInt("roomId");
                        int noise = noiseObject.getInt("noise");
                        String time = noiseObject.getString("time");
                        Log.d("noise", String.valueOf(noise));
                        if(noise >= 80 && i == roomArray.length()-1){
                            flag = true;
                            roomName += "방 번호" + id + " ";
                        }
                    }
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
            if(flag){
                Notifi = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle("함께하시계")
                        .setContentText(roomName + "층간 소음 발생!!")
                        .setSmallIcon(R.drawable.main_icon)
                        .setTicker("알림!!!")
                        .setContentIntent(pendingIntent)
                        .setChannelId(idd)
                        .build();

                //소리추가
                Notifi.defaults = Notification.DEFAULT_SOUND;

                //알림 소리를 한번만 내도록
                Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;

                //확인하면 자동으로 알림이 제거 되도록
                Notifi.flags = Notification.FLAG_AUTO_CANCEL;


                Notifi_M.notify( 777 , Notifi);
            }

        }
    };



}
