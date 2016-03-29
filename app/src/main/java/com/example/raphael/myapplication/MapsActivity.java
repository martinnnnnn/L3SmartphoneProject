package com.example.raphael.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    public ArrayList<LatLng> points;
    public Polyline line;
    public Projection projection;
    SupportMapFragment mapFragment;
    public Map<Marker, Waypoint> allMarkersMap = new HashMap<Marker, Waypoint>();
    AsyncReception reception;
    AsyncEnvoi envoi;
    Marker temp;
    TextView vitesse;
    float time=0;
    long lastTime = 0;
    long currentTime = 0;
    RadioButton radiobutton1;
    RadioButton radiobutton2;
    RadioButton radiobutton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        vitesse = (TextView)findViewById((R.id.textView2));

        radiobutton1 = (RadioButton)findViewById(R.id.radioButton1);
        radiobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        radiobutton2 = (RadioButton)findViewById(R.id.radioButton2);
        radiobutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ViewTwo.class);
                startActivity(intent);
            }
        });

        radiobutton3 = (RadioButton)findViewById(R.id.radioButton3);
        radiobutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewThree.class);
                startActivity(intent);
            }
        });



        SeekBar zoom = (SeekBar)findViewById(R.id.seekBar);
        zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMap.moveCamera(CameraUpdateFactory.zoomTo(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        reception = new AsyncReception();
        reception.execute();

        //envoi = new AsyncEnvoi();
        //envoi.execute();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        projection = mMap.getProjection();

        line = mMap.addPolyline(new PolylineOptions()
                .width(5)
                .color(Color.RED));

        points = new ArrayList<LatLng>();
        line.setPoints(points);



        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window, null);
                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();
                // Getting reference to the TextView to set latitude
                TextView speed = (TextView) v.findViewById(R.id.speed);
                // Setting the latitude
                speed.setText(" "+Integer.toString(allMarkersMap.get(arg0).getSpeed()));
                return v;
            }
        });

        long temps = 1000;                      // délai avant de répéter la tache : 2000 = 2  seconde
        long startTime = 0;                    // délai avant la mise en route (0 demarre immediatement)
        Timer timer = new Timer();             // création du timer
        TimerTask tache = new TimerTask() {     // création et spécification de la tache à effectuer
            @Override
            public void run() {
                   // ici se qui doit être effectué

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        marquerPosition();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(tache, startTime, temps);  // ici on lance la mecanique


    }



    public void marquerPosition(){
        if(reception.receptionHasStarted) {
            LatLng leNouveauPoint = new LatLng(reception.f_latitude, reception.f_longitude);
            if(temp == null) {
                temp = mMap.addMarker(new MarkerOptions().position(leNouveauPoint)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.droneicon))
                        .rotation(reception.f_boussole));
            }
            else{
                temp.setPosition(leNouveauPoint);
                temp.setRotation(reception.f_boussole);
            }
            points.add(new LatLng(reception.f_latitude, reception.f_longitude));

            vitesse.setText("vitesse : "+reception.s_vitesse+" noeuds/s");
            line.setPoints(points);
        }
    }

    public void buttonFind(View v){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(reception.f_latitude, reception.f_longitude)));
    }
}