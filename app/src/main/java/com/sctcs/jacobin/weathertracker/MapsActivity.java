package com.sctcs.jacobin.weathertracker;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener{

    private Marker marker;
    private GoogleMap mMap;
    private EditText editText;
    private Button searchbutton;
    private String api_id;




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

        return  true;
    }
}
