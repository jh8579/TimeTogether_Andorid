package com.jinojino.happytogether;

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

public class HttpThread extends Thread {
    String result;
    int id;

    public HttpThread(){

    }

    public void run(){
        HttpClient httpClient = new DefaultHttpClient();
        String urlString = "http://timetotogether.us-east-1.elasticbeanstalk.com/getNoise";
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
    }

    public String getResult(){
        return result;
    }
}
