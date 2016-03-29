package com.example.raphael.myapplication;


import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class ViewThree extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Accelerometer acc;
    SeekBar seekBar;
    SeekBar seekBar2;
    TextView text;
    TextView text2;
    private LatLng position = new LatLng(46.141229, -1.168985);
    private LatLng lastPosition = new LatLng(46.141229, -1.168985);
    private static final LatLng Minime =
            new LatLng(46.141229, -1.168985);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_three);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        text = (TextView) findViewById(R.id.text);
        acc = new Accelerometer(this, this.getBaseContext());
        mapFragment.getMapAsync(this);

        //mMap.setLocationSource();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Minime, 20));

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                drawLineOnMap();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 25));
            }
        });
    }

    public void moveOnMap(){
        lastPosition = position;
        double lat = acc.vitesse/10000000*Math.sin(acc.rotation);
        double lon = acc.vitesse/10000000*Math.sin(90 -acc.rotation);
        position  = new LatLng(position.latitude + lat,position.longitude + lon);
    }

    public void drawLineOnMap(){
        moveOnMap();
        mMap.addPolyline((new PolylineOptions()).add(lastPosition, position)
                .width(5).color(Color.BLUE).geodesic(true));
    }
}
