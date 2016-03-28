package com.example.raphael.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    boolean edit = false;

    boolean needToRecharge;
    String actualJSON;

    private GoogleMap mMap;
    ArrayList<LatLng> points;
    Polyline line;

    SupportMapFragment mapFragment;
    private Map<Marker, Waypoint> allMarkersMap = new HashMap<Marker, Waypoint>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needToRecharge = false;
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        line = mMap.addPolyline(new PolylineOptions()
                .width(5)
                .color(Color.RED));

        points = new ArrayList<LatLng>();
        line.setPoints(points);

        //Rechargement apres destruction de l'activité (rotation par exemple)
        if(needToRecharge){
            rechargeMarker();
        }

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
                LatLng latLng = arg0.getPosition();
                TextView speed = (TextView) v.findViewById(R.id.speed);
                speed.setText(" " + Integer.toString(allMarkersMap.get(arg0).getSpeed()));
                return v;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker arg0) {
                System.out.println("CLICK ------------------------------------------++++++++++++");
                final Marker m = arg0;

                final EditText input = new EditText(MapsActivity.this);

                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(Integer.toString(allMarkersMap.get(arg0).getSpeed()));
                input.setSelection(input.getText().length());

                final Button deleteBtn = new Button(MapsActivity.this);
                deleteBtn.setText("Supprimer");
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m.hideInfoWindow();
                        points.remove(allMarkersMap.get(m).getId());
                        allMarkersMap.remove(m);
                        m.remove();
                        updatePolyline();
                    }
                });

                input.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        String text = input.getText().toString();
                        if (text.isEmpty()) {
                            text = "0";
                        }
                        allMarkersMap.get(m).setSpeed(Integer.parseInt(text));
                        //Rouvrir pour mettre a jour le contenu
                        m.hideInfoWindow();
                        m.showInfoWindow();
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                LinearLayout lnl = new LinearLayout(MapsActivity.this);
                lnl.setOrientation(LinearLayout.VERTICAL);
                lnl.setLayoutParams(lp);
                lnl.addView(input);
                lnl.addView(deleteBtn);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
                alertDialog.setTitle("Vitesse");
                alertDialog.setMessage("Entrez vitesse en noeuds: ");
                alertDialog.setView(lnl); // uncomment this line
                alertDialog.show();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                Marker temp = mMap.addMarker(new MarkerOptions().position(arg0));
                temp.setDraggable(true);
                if(allMarkersMap.size() == 0)
                    temp.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.start_icon));
                points.add(arg0);
                allMarkersMap.put(temp, new Waypoint(10));
                updatePolyline();
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                points.set(allMarkersMap.get(marker).getId(), marker.getPosition());
                updatePolyline();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //points.set(allMarkersMap.get(marker).getId(), marker.getPosition());
                //updatePolyline();
            }
        });

    }

    //Permet de mettre à jour la polyline affichée sur la map
    private void updatePolyline() {
        line.setPoints(points);
    }

    public void onClickValidate(View v) {
        System.out.println(NMEAUtil.toNMEA(allMarkersMap));
    }

    public void onClickEdit(View v) {
        mMap.getUiSettings().setAllGesturesEnabled(edit);
        edit = !edit;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("Waypoints", JsonUtil.toJSon(allMarkersMap));
        // etc.
        super.onSaveInstanceState(savedInstanceState);
    }
    //onRestoreInstanceState
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        actualJSON = savedInstanceState.getString("Waypoints");
        if(actualJSON.length() > 0)
            needToRecharge = true;
    }

    //Charge les markers a partir d'une string json
    private void rechargeMarker() {
        allMarkersMap = JsonUtil.toMarkers(mMap, points, actualJSON);
        updatePolyline();
        needToRecharge = false;
    }

}