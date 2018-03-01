package com.sctcs.jacobin.weathertracker;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkUtils {

    private static final  String BASE_URL="http://api.openweathermap.org/data/2.5/weather?";
    private  static final String QUERY_LANG="lat";
    private  static final String QUERY_LONG="lon";
    private  static final String QUERY_ID="appid";



    static  URL buid_url(LatLng latLng,String apiid)
    {
       Uri  uri = Uri.parse(BASE_URL).buildUpon()
               .appendQueryParameter(QUERY_LANG,latLng.latitude+"")
               .appendQueryParameter(QUERY_LONG,latLng.longitude+"")
               .appendQueryParameter(QUERY_ID,apiid).build();
       URL url = null;

       try {
           url =new URL(uri.toString());
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

       Log.v("TAG",url.toString());


       return url;


    }






}
