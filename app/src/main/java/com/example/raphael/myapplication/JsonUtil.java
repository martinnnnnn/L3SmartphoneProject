package com.example.raphael.myapplication;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Raphael on 28/03/2016.
 */
public class JsonUtil {

    public static String toJSon(Map<Marker, Waypoint> list) {
        try {
            JSONArray jsonArr = new JSONArray();

            Marker key;
            Waypoint value;
            JSONObject newWaypoint;
            LatLng pos;

            //Map triée par les id
            Map<Marker, Waypoint> myMap = WaypointComparator.sortByValue(list);

            for(Map.Entry<Marker, Waypoint> entry : myMap.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();

                newWaypoint = new JSONObject();
                newWaypoint.put("id", value.getId());
                newWaypoint.put("speed", value.getSpeed());
                pos = key.getPosition();
                newWaypoint.put("lat", pos.latitude);
                newWaypoint.put("lon", pos.longitude);

                jsonArr.put(newWaypoint);
            }
            System.out.println(jsonArr.toString());
            return jsonArr.toString();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public static Map<Marker, Waypoint> toMarkers(GoogleMap mMap, ArrayList<LatLng> points, String json) {
        Map<Marker, Waypoint> res = new HashMap<Marker, Waypoint>();

        try {
            JSONArray jArr = new JSONArray(json);
            JSONObject obj;
            Marker marker;
            Waypoint wayp;
            LatLng latlng;

            for (int i = 0; i < jArr.length(); i++) {
                obj = jArr.getJSONObject(i);
                latlng = new LatLng(obj.getDouble("lat"), obj.getDouble("lon"));
                wayp = new Waypoint(obj.getInt("id"), obj.getInt("speed"));
                marker = mMap.addMarker(new MarkerOptions().position(latlng));
                marker.setDraggable(true);
                if(i == 0)
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.start_icon));
                points.add(latlng);
                res.put(marker, wayp);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return res;


    }
}