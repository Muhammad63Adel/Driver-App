package com.example.driver;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;


public class google_maps extends FragmentActivity implements OnMapReadyCallback ,  RoutingListener {

    GoogleMap mMap;
    FusedLocationProviderClient client;
    MarkerOptions place1, place2;
    // Polyline currentPolyline;
    Button getDirection;

    public double Latitude = 10;
    public double Longitude = 20;
    public double target_Latitude = 10;
    public double target_Longitude = 20;
    LatLng driver_latLng;
    LatLng target_latLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        getDirection = findViewById(R.id.btnGetDirection);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String lat = bundle.getString("latitude");
        String lon = bundle.getString("longitude");
        assert lat != null;
        target_Latitude =Double.parseDouble(lat);
        assert lon != null;
        target_Longitude =Double.parseDouble(lon);
        Toast.makeText(google_maps.this, " target_Latitude = " + target_Latitude +"\n"+" target_Longitude = " + target_Longitude, Toast.LENGTH_SHORT).show();

        polylines = new ArrayList<>();
        place1 = new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(target_Latitude, target_Longitude)).title("Location 2");


        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(google_maps.this, "find route", Toast.LENGTH_SHORT).show();

            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, " can noفقققققققثt take permission  ", Toast.LENGTH_SHORT).show();
            return;
        }
        client = LocationServices.getFusedLocationProviderClient(this);
        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        //mMap.addMarker(place1);
       // mMap.addMarker(place2);

        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                    driver_latLng = new LatLng(Latitude,Longitude);
                    target_latLng = new LatLng(target_Latitude,target_Longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(driver_latLng)
                            .title("my location")
                            .icon(getMarkerIcon("#F3174C")));
                    mMap.addMarker(new MarkerOptions()
                            .position(target_latLng)
                            .title("my target")
                            .icon(getMarkerIcon("#1ab849")));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target_latLng , 9), 3500, null);
                    Toast.makeText(google_maps.this, "latitude is "+ Latitude+"\n"+"Longitude is" + Longitude, Toast.LENGTH_LONG).show();
                    getRouteToMarker(target_latLng);
                }
              }
            public BitmapDescriptor getMarkerIcon(String color) {
                float[] hsv = new float[3];
                Color.colorToHSV(Color.parseColor(color), hsv);
                return BitmapDescriptorFactory.defaultMarker(hsv[0]);
            }
        });
    }

    private void getRouteToMarker(LatLng target_latLng) {
        Routing routing = new Routing.Builder()
                .key("AIzaSyC8muF-_fw9XG2AJXSnUMDsAUrDblkFpyU")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(driver_latLng, target_latLng)
                .build();
        routing.execute();

    }


    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e) {

        // The Routing request failed

        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingCancelled() {
    }



    private void erasePolyLines(){
        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }
}
