package com.pcchecker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.CircularBounds;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.SearchByTextRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.slider.Slider;
import com.pcchecker.data.ShopDatabase;
import com.pcchecker.model.Shop;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StoreMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;

    private Circle mSearchCircle;
    private TextView mTvRadiusValue;
    private LatLng mCurrentUserLocation;
    private double mCurrentRadiusKm = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCBSvzJwKVeyIjIiICkJBBSIJ0j7hQo0N8");
        }
        placesClient = Places.createClient(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        mTvRadiusValue = findViewById(R.id.tv_radius_value);
        Slider radiusSlider = findViewById(R.id.slider_radius);
        radiusSlider.addOnChangeListener((slider, value, fromUser) -> {
            mCurrentRadiusKm = value;
            mTvRadiusValue.setText(String.format(Locale.US, "%.0f km", value));
            updateCircleAndSearch();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setupNavigation();
    }

    private void setupNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_shops);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_builder) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_saved) {
                startActivity(new Intent(this, SavedBuildsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_shops) {
                return true;
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    private void updateCircleAndSearch() {
        if (mMap == null || mCurrentUserLocation == null) return;

        mMap.clear();
        mSearchCircle = mMap.addCircle(new CircleOptions()
                .center(mCurrentUserLocation)
                .radius(mCurrentRadiusKm * 1000)
                .strokeWidth(2f)
                .strokeColor(ContextCompat.getColor(this, R.color.primary))
                .fillColor(0x226366F1));

        findActualNearbyShopsWithRadius();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        zoomToUserLocation();
    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            loadShopMarkers();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                mCurrentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentUserLocation, 12f));
                updateCircleAndSearch();
            } else {
                loadShopMarkers();
            }
        });
    }

    private void findActualNearbyShopsWithRadius() {
        if (mCurrentUserLocation == null) return;

        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        
        SearchByTextRequest searchRequest = SearchByTextRequest.builder("computer components electronics shop", placeFields)
                .setLocationBias(CircularBounds.newInstance(mCurrentUserLocation, mCurrentRadiusKm * 1000))
                .setMaxResultCount(15)
                .build();

        placesClient.searchByText(searchRequest)
                .addOnSuccessListener((response) -> {
                    for (Place place : response.getPlaces()) {
                        if (place.getLatLng() != null) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(place.getLatLng())
                                    .title(place.getName())
                                    .snippet(place.getAddress()));
                        }
                    }
                })
                .addOnFailureListener((e) -> loadShopMarkers());
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void loadShopMarkers() {
        List<Shop> shops = ShopDatabase.getShops();
        for (Shop shop : shops) {
            LatLng location = new LatLng(shop.getLatitude(), shop.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(shop.getName())
                    .snippet(shop.getAddress()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                zoomToUserLocation();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
