package com.sctcs.jacobin.weathertracker;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener {

    private Marker marker;
    private GoogleMap mMap;
    private EditText editText;
    private Button searchbutton;
    private String api_id;

    private static  final  int LOADER_ID =3421;

    private String response_from;

    FirebaseDatabase database;
    DatabaseReference myrefernce;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editText=(EditText) findViewById(R.id.searchbox);
        searchbutton=(Button) findViewById(R.id.searchbutton);

        api_id=getResources().getString(R.string.api_id);

        database=FirebaseDatabase.getInstance();
        myrefernce=database.getReference("weather");







        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handle();
            }
        });




    }





    void handle()
    {
        if (marker!=null)
        {
            marker.remove();
        }
        List<Address>  addresses=null;
        String location = editText.getText().toString();

        if (location != null || !location.equals(" "))
        {
            Geocoder geocoder = new Geocoder(this );
            try
            {
                  addresses= geocoder.getFromLocationName(location,1);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            Address address=addresses.get(0);
            LatLng latLng= new LatLng(address.getLatitude(),address.getLongitude());
            marker=mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));



        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
       marker= mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Centre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = marker.getPosition();


        Toast.makeText(this,"LOCATION: " + latLng.latitude +"," + latLng.longitude +"" ,Toast.LENGTH_SHORT).show();
        //TODO :  IMPLEMENT WEATHER API  CALL
        URL callurl= NetworkUtils.buid_url(latLng,api_id);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.GET, callurl.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("TAG",response.substring(0,20));

                        try {
                            JSONObject object=new JSONObject(response);
                            JSONArray weather_array= object.getJSONArray("weather");
                            JSONObject first_object=weather_array.getJSONObject(0);
                            response_from=first_object.getString("main");
                            myrefernce.setValue(response_from);
                            Log.v("TAG",response_from);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("TAG",error.toString());

                    }
                });
                queue.add(request);






        return  true;
    }



}
