package com.example.tpmaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Permet de prendre la position du user

    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng Saint_quentin_piscine;
    private LatLng Saint_quentin_cinema;
    private LatLng Saint_quentin_theatre;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    LocationManager localisations;

    private LatLng latLng;
    private TextView battery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        battery = (TextView)findViewById(R.id.batteryLevel);
        this.registerReceiver(this.batterylevelReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

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

        // Récupère a position de la piscine de saint quentin

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        localisations = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Saint_quentin_piscine = new LatLng(45.1624635, 5.7147129);
        //Saint_quentin_piscine = new LatLng(49.856049899999995, 3.3034059);
        Saint_quentin_cinema = new LatLng(50.266667, 1.666667);
        //Saint_quentin_cinema = new LatLng(49.8561059, 3.3035434);
        Saint_quentin_theatre = new LatLng(49.8489, 3.2876);


        mMap.addMarker(new MarkerOptions().position(Saint_quentin_piscine).title("Piscine"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Saint_quentin_piscine));

        mMap.addMarker(new MarkerOptions().position(Saint_quentin_cinema).title("Cinema"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Saint_quentin_cinema));

        mMap.addMarker(new MarkerOptions().position(Saint_quentin_theatre).title("Theatre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Saint_quentin_theatre));

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
        String localizedDate = df.format(date);
        System.out.println(localizedDate);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                openActivity();

                try {
                    //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    //Date date = new Date(location.getTime());
                    //Log.d("GPS", sdf.format(date));
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    //System.out.println(Dist(location.getLatitude(), location.getLongitude(), Saint_quentin_piscine.latitude, Saint_quentin_piscine.longitude));
                    if (Dist(location.getLatitude(), location.getLongitude(), Saint_quentin_piscine.latitude, Saint_quentin_piscine.longitude)<= 50){
                        reslong(latLng);
                    }else if(Dist(location.getLatitude(), location.getLongitude(), Saint_quentin_cinema.latitude, Saint_quentin_cinema.longitude)<= 0.1){
                        reslong(latLng);

                    }else if (Dist(location.getLatitude(), location.getLongitude(), Saint_quentin_theatre.latitude, Saint_quentin_theatre.longitude)<= 50){
                        reslong(latLng);
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {

            }
            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }


    public void reslong(LatLng latLng){


        mMap.addMarker(new MarkerOptions().position(latLng).title("Ma position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        String phoneNumber = "0754412338";
        String myLatitude = String.valueOf(latLng.latitude);
        String myLongitude = String.valueOf(latLng.longitude);

        String message = "Votre enfant est bien arrivé à l'endroit qu'il partait des coordonnées : Latitude = " + myLatitude + "Longitude = " + myLongitude + getTitle();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private double Dist(double lat1, double lon1, double lat2, double lon2)
    {
        int R = 6373; // radius of the earth in kilometres
        double lat1rad = Math.toRadians(lat1);
        double lat2rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2-lat1);
        double deltaLon = Math.toRadians(lon2-lon1);

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.cos(lat1rad) * Math.cos(lat2rad) *
                        Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c;
        return d;
    }


    //Recuperation de la charge de battery
    private BroadcastReceiver batterylevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;

            battery.setText(String.valueOf(batteryPct)+"%");


            if (batteryPct == 15){
                String message = "Le téléphone de votre enfant est déchargé";
                SmsManager smsManager = SmsManager.getDefault();
                String phoneNumber = "0754412338";
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }else {

                String message = "Votre fils vient de desactiver son téléphone";
                String phoneNumber = "0754412338";
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }
        }
    };

    public void openActivity(){
        Intent intent = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(intent);

//        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
//        startActivity(intent);
    }

}