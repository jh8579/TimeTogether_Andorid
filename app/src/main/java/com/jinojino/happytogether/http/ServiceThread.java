package com.jinojino.happytogether.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

import static android.content.ContentValues.TAG;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;
    String result;
    int id;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        HttpClient httpClient = new DefaultHttpClient();
        String urlString = "http://togethertime-env.pr5tzjsask.ap-northeast-2.elasticbeanstalk.com/getNoise";

        while(isRun){
            try {
                URI url = new URI(urlString);
                HttpGet httpGet = new HttpGet();
                httpGet.setURI(url);
                HttpResponse response = httpClient.execute(httpGet);
                String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);


                Log.d(TAG, responseString);
                result = responseString;

            } catch (URISyntaxException e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄
            try{
                //Thread.sleep(10000*6*5); //5분씩 쉰다.
                Thread.sleep(10000); //5분씩 쉰다.
            }catch (Exception e) {}
        }
    }
    public String getResult(){
        return result;
    }
}
