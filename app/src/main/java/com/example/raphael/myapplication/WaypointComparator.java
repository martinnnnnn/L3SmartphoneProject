package com.example.raphael.myapplication;

/**
 * Created by Raphael on 28/03/2016.
 */

import com.google.android.gms.maps.model.Marker;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class WaypointComparator {// implements Comparator<Waypoint> {
    /*
    private SortedMap<Marker, Waypoint> map;

    public WaypointComparator(SortedMap<Marker, Waypoint> map) {
        this.map = map;
    }


    public WaypointComparator(){

    }

    @Override
    public int compare(Waypoint a, Waypoint b) {
        return ((Integer)a.getId()).compareTo(b.getId());
    }
    */

    //public static <Marker, Waypoint extends Comparable<? super Waypoint>> Map<Marker, Waypoint> sortByValue( Map<Marker, Waypoint> map )
    public static Map<Marker, Waypoint> sortByValue( Map<Marker, Waypoint> map )
    {
        List<Map.Entry<Marker, Waypoint>> list = new LinkedList<>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<Marker, Waypoint>>() {
            @Override
            public int compare(Map.Entry<Marker, Waypoint> o1, Map.Entry<Marker, Waypoint> o2) {
                return ((Integer) (o1.getValue()).getId()).compareTo(o2.getValue().getId());
            }
        });

        Map<Marker, Waypoint> result = new LinkedHashMap<>();
        for (Map.Entry<Marker, Waypoint> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
