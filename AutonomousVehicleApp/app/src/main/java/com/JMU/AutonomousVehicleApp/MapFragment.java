package com.JMU.AutonomousVehicleApp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    MapView mapView;
    GoogleMap map;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapFragView = inflater.inflate(R.layout.fragment_map, container, false);

        return mapFragView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    //setting up google maps on view
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng xlabs = new LatLng(38.431928, -78.875965);
        //LatLng xlabs1 = new LatLng(38.432146, -78.876106);
        //LatLng xlabs2 = new LatLng(38.432129, -78.876287);
        //LatLng xlabs3 = new LatLng(38.431602, -78.876174);

        MarkerOptions options = new MarkerOptions();
        options.position(xlabs).title("X-Labs");
        /*map.addPolyline(new PolylineOptions()
                .add(xlabs1, xlabs2, xlabs3, xlabs)
                .width(1)
                .color(android.R.color.holo_red_light)
                .geodesic(false)
                .clickable(false)
        );*/
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(xlabs,17.0f));
    }
}
