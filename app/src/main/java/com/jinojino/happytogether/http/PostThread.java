package com.jinojino.happytogether.http;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

import static android.content.ContentValues.TAG;

public class PostThread extends Thread {
    String object;
    int bright;
    int power;

    public PostThread(String object, int bright, boolean power){
        this.object = object;
        this.bright = bright;
        if(power){
            this.power=1;
        }else{
            this.power=0;
        }
    }

    public void run(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://togethertime-env.pr5tzjsask.ap-northeast-2.elasticbeanstalk.com/put"+object);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create() //객체 생성...
                .setCharset(Charset.forName("UTF-8")) //인코딩을 UTF-8로.. 다들 UTF-8쓰시죠?
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addTextBody("bright",String.valueOf(bright));
        builder.addTextBody("power",String.valueOf(power));

        try {
            post.setEntity(builder.build());
            HttpResponse response = httpClient.execute(post);
            Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
