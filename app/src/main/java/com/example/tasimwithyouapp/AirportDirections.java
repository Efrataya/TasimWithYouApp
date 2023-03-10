package com.example.tasimwithyouapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class LocationData {
    public double latitude;
    public double longitude;
    public String address;

    public LocationData(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
}


class PlaceAutoCompleteWatcher implements android.text.TextWatcher {

    private Context context;
    private PlacesClient client;
    private AutocompleteSessionToken token;
    private ArrayAdapter<String> adapter;

    public PlaceAutoCompleteWatcher(Context context, PlacesClient client, AutocompleteSessionToken token, ArrayAdapter<String> adapter) {
        this.context = context;
        this.client = client;
        this.token = token;
        this.adapter = adapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(token)
                    .setQuery(s.toString())
                    .setTypesFilter(
                            List.of("establishment")
                    )
                    .setCountry("IL")
                    .setLocationBias(
                            RectangularBounds.newInstance(
                                    new LatLng(31.0, 34.0),
                                    new LatLng(32.0, 35.0)
                            )
                    )
                    .build();

            client.findAutocompletePredictions(request).addOnSuccessListener(response -> {
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                List<String> addresses = new ArrayList<>();
                for (com.google.android.libraries.places.api.model.AutocompletePrediction prediction : predictions) {
                    addresses.add(prediction.getFullText(null).toString().split(",")[0]);
                }
                adapter.clear();
                adapter.addAll(addresses);
                adapter.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                Toast.makeText(context, "Failed to fetch predictions", Toast.LENGTH_SHORT).show();
            });
        }

    }

    public void dispose() {
        client = null;
        token = null;
        adapter = null;
        context = null;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

public class AirportDirections extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MAP_PERMISSIONS_ID = 1;
    private GoogleMap map;
    private FusedLocationProviderClient locationClient;

    private Marker userMarker;
    private static final String key = "AIzaSyBJgCGmmX4vMmyrFF4XijHrlYd-wpEUT08";

    private PlaceAutoCompleteWatcher placeAutoCompleteWatcher;

    public void setupAutoComplete(AutoCompleteTextView autoCompleteTextView) {
        Places.initialize(getApplicationContext(), key);
        PlacesClient client = Places.createClient(getApplicationContext());

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line
        );

        autoCompleteTextView.addTextChangedListener(
                new PlaceAutoCompleteWatcher(
                        getApplicationContext(),
                        client,
                        token,
                        adapter
                )
        );

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            getDirections(autoCompleteTextView.getText().toString());
        });

        autoCompleteTextView.setAdapter(adapter);

    }

    private double distanceBetween(com.google.maps.model.LatLng point1, com.google.maps.model.LatLng point2) {
        // Use the Haversine formula to calculate the distance between two points on Earth
        double earthRadius = 6371; // kilometers
        double dLat = Math.toRadians(point2.lat - point1.lat);
        double dLng = Math.toRadians(point2.lng - point1.lng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(point1.lat)) * Math.cos(Math.toRadians(point2.lat)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    public boolean isRouteWithinRadius(DirectionsResult directionsResult, LatLng center, double radius) {
        double distance = 0;
        com.google.maps.model.LatLng lastPoint = null;

        // Calculate the distance of the entire route
        for (com.google.maps.model.LatLng point : directionsResult.routes[0].overviewPolyline.decodePath()) {
            if (lastPoint != null) {
                distance += distanceBetween(lastPoint, point);
            }
            lastPoint = point;
        }

        // Check if the distance of the entire route is within the radius from the center
        return distance <= radius;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (placeAutoCompleteWatcher != null) {
            placeAutoCompleteWatcher.dispose();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = "he_IL";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_temp);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        AutoCompleteTextView autoComplete = findViewById(R.id.dest);
        setupAutoComplete(autoComplete);
    }

    public static final LatLng center = new LatLng(31.999, 34.864);

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        LatLngBounds BEN_GURION_BOUNDS = new LatLngBounds(
                new LatLng(31.9311, 34.7389),    // South west corner
                new LatLng(32.0056, 34.8950));   // North east corner
        googleMap.setLatLngBoundsForCameraTarget(BEN_GURION_BOUNDS);
        googleMap.setMinZoomPreference(17f);
        googleMap.setMaxZoomPreference(20f);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setTrafficEnabled(false);
        googleMap.setOnMapClickListener(latLng -> {
            if (userMarker != null) {
                userMarker.remove();
            }
            System.out.println(latLng);
            userMarker = map.addMarker(new MarkerOptions().position(latLng));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        });

        trackUserLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MAP_PERMISSIONS_ID) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                trackUserLocation();
            }
        }
    }


    private class FetchDirectionsTask extends AsyncTask<Object, Void, List<com.google.maps.model.LatLng>> {

        Exception e;

        public FetchDirectionsTask() {
            super();
            this.e = null;
        }

        @Override
        protected List<com.google.maps.model.LatLng> doInBackground(Object... objects) {
            List<com.google.maps.model.LatLng> polyLine = null;
            try {
                GeoApiContext context = new GeoApiContext.Builder()
                        .apiKey(key)
                        .build();

                DirectionsResult result = DirectionsApi.newRequest(context)
                        .departureTimeNow()
                        .waypoints(
                                new DirectionsApiRequest.Waypoint((com.google.maps.model.LatLng) objects[0]),
                                new DirectionsApiRequest.Waypoint((String) objects[1]))
                        .mode(TravelMode.WALKING)
                        .origin((com.google.maps.model.LatLng) objects[0])
                        .destination((String) objects[1]).await();

                if (!isRouteWithinRadius(result, center, 10)) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "היעד המבוקש נמצא מחוץ לגבולות שדה התעופה", Toast.LENGTH_LONG).show();
                    });
                    return null;
                }
                if (result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    polyLine = route.overviewPolyline.decodePath();
                }

            } catch (IOException | ApiException | InterruptedException e) {
                this.e = e; // save exception
            }
            return polyLine;
        }

        @Override
        protected void onPostExecute(List<com.google.maps.model.LatLng> route) {
            if (e != null) {
                // there was an error
                e.printStackTrace();

                if (e.getMessage() != null)
                    Snackbar.make(findViewById(R.id.mapFragLayout),
                            e.getMessage(), Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(findViewById(R.id.mapFragLayout),
                            "לא ניתן לנווט ליעד המבוקש", Snackbar.LENGTH_LONG).show();
                return;
            }

            if (route != null) {
                List<LatLng> polyLineGMS = new ArrayList<>();
                for (com.google.maps.model.LatLng point : route)
                    polyLineGMS.add(new LatLng(point.lat, point.lng));
                PolylineOptions opts = new PolylineOptions()
                        .color(Color.RED)
                        .addAll(polyLineGMS);
                map.addPolyline(opts);
                LatLngBounds.Builder boundsBuilder = new
                        LatLngBounds.Builder();
                for (LatLng point : polyLineGMS) {
                    boundsBuilder.include(point);
                }
            }
        }
    }


    public void getDirections(String dest) {
        System.out.println("Fetching directions :");
        System.out.println("Origin " + currentLocation);
        System.out.println("Dest " + dest);
        new FetchDirectionsTask().execute(new com.google.maps.model.LatLng(
                currentLocation.latitude,
                currentLocation.longitude
        ), dest);
    }


    LatLng currentLocation;
    // Check the route bounds

    private void trackUserLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                LocationManager locationManager = getSystemService(LocationManager.class);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return;

                LocationListener listener = location -> {
                    Geocoder geocoder = new Geocoder(AirportDirections.this,
                            Locale.getDefault());
                    try {
                        LocationData locationData = getLocationString(location, geocoder);
                        moveToLocation(locationData);
                        addUserMarker(locationData.latitude, locationData.longitude);
                        EditText et = findViewById(R.id.src);
                        et.setText(locationData.address);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        30,
                        5,
                        listener);
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    public LocationData getLocationString(
            Location location,
            Geocoder geocoder
    ) throws IOException {
        List<Address> list =
                geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        Address current = list.get(0);
        double lat = current.getLatitude();
        double lng = current.getLongitude();
        String address = current.getAddressLine(0);
        return new LocationData(
                lat,
                lng,
                address
        );
    }

    private Marker addMapMarker(double lat, double lng) {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng))
                .title("My location");
        return map.addMarker(marker);
    }

    private void addUserMarker(double lat, double lng) {
        if (userMarker != null)
            userMarker.remove();
        userMarker = addMapMarker(lat, lng);
        currentLocation = new LatLng(lat, lng);
    }

    private void moveToLocation(LocationData locationData) {
        LatLng coordinates = new LatLng(locationData.latitude, locationData.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        map.addMarker(new MarkerOptions().position(coordinates)
                .title(locationData.address));
    }


    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                MAP_PERMISSIONS_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


}
