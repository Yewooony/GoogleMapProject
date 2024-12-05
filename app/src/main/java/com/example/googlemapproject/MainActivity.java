package com.example.googlemapproject;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.googlemapproject.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ActivityMainBinding binding;
    private CsvHelper csvHelper;
    private PreferencesHelper preferencesHelper;

    private PlaceType currentPlaceType = null;
    private List<Place> allPlaces = new ArrayList<>();
    private Map<Marker, Place> markers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 툴바 설정 설정
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darkblue)));
        setTitle("  경기도 지도 앱");

        // Initialize helpers
        csvHelper = new CsvHelper(this);
        preferencesHelper = new PreferencesHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Setup map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setupSearchBar();
        setupBottomNavigation();
        loadAllPlaces();
    }

    private void setupSearchBar() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchPlaces(v.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null); // 아이콘 색상 변경 비활성화

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_museum) {
                currentPlaceType = PlaceType.ART;
            } else if (itemId == R.id.nav_cafe) {
                currentPlaceType = PlaceType.CAFE;
            } else if (itemId == R.id.nav_sports) {
                currentPlaceType = PlaceType.GYM;
            } else if (itemId == R.id.nav_library) {
                currentPlaceType = PlaceType.LIBRARY;
            } else if (itemId == R.id.nav_restaurant) {
                currentPlaceType = PlaceType.RESTAURANT;
            }
            updateMarkers();
            return true;
        });
    }

    private void loadAllPlaces() {
        // Load places in background
        new Thread(() -> {
            List<Place> places = csvHelper.loadAllPlaces();
            runOnUiThread(() -> {
                allPlaces.clear();
                allPlaces.addAll(places);
                restoreFavorites();
                updateMarkers();
            });
        }).start();
    }

    private void restoreFavorites() {
        for (Place place : allPlaces) {
            String placeId = place.getName() + "_" + place.getAddress();
            place.setFavorite(preferencesHelper.isFavorite(placeId));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configure map settings
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMarkerClickListener(marker -> {
            // 마커의 정보 창을 표시합니다
            marker.showInfoWindow();
            binding.favorite.setImageResource(R.drawable.ic_star_border);

            // markers Map에서 해당 마커에 연결된 Place 객체를 가져옵니다
            Place place = markers.get(marker);

            if (place != null) {
                // 주소 표시 영역을 보이게 만들고 주소를 설정합니다
                binding.addressView.setVisibility(View.VISIBLE);
                binding.addressTextView.setText(place.getAddress());

                // 즐겨찾기 버튼에 클릭 리스너를 설정합니다
                binding.favorite.setOnClickListener(v -> {
                    // 즐겨찾기 아이콘을 채워진 별로 변경합니다
                    binding.favorite.setImageResource(R.drawable.ic_star);

                    // 사용자에게 즐겨찾기 추가를 알리는 토스트 메시지를 표시합니다
                    Toast.makeText(
                            this,
                            "즐겨찾기에 추가되었습니다.",
                            Toast.LENGTH_SHORT
                    ).show();

                    // 필요하다면 여기에 즐겨찾기 데이터를 저장하는 로직을 추가할 수 있습니다
                    // 예: saveToFavorites(place);
                });

                return true; // 이벤트가 처리되었음을 알립니다
            }

            return false; // Place가 null인 경우 기본 동작을 실행합니다
        });

        // Check location permission
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            moveToCurrentLocation();
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void moveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                updateMarkers();
            }
        });
    }

    private void searchPlaces(String query) {
        mMap.clear();
        markers.clear();

        List<Place> searchResults = allPlaces.stream()
                .filter(place ->
                        place.getName().toLowerCase().contains(query.toLowerCase()) ||
                                place.getRegion().toLowerCase().contains(query.toLowerCase()) ||
                                place.getAddress().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        displayPlaces(searchResults);

        if (!searchResults.isEmpty()) {
            Place firstResult = searchResults.get(0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(firstResult.getLatitude(), firstResult.getLongitude()),
                    DEFAULT_ZOOM
            ));
        }
    }

    private void updateMarkers() {
        mMap.clear();
        markers.clear();

        List<Place> filteredPlaces = allPlaces.stream()
                .filter(place -> currentPlaceType == null || place.getType() == currentPlaceType)
                .collect(Collectors.toList());

        displayPlaces(filteredPlaces);
    }

    private void displayPlaces(List<Place> places) {
        for (Place place : places) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(place.getLatitude(), place.getLongitude()))
                    .title(place.getName())
                    .icon(place.isFavorite() ?
                            BitmapDescriptorFactory.fromResource(R.drawable.ic_star) :
                            BitmapDescriptorFactory.fromResource(R.drawable.ic_mark));

            Marker marker = mMap.addMarker(markerOptions);
            if (marker != null) {
                markers.put(marker, place);
            }
        }
    }

    private void toggleFavorite(Place place) {
        place.setFavorite(!place.isFavorite());
        String placeId = place.getName() + "_" + place.getAddress();

        if (place.isFavorite()) {
            preferencesHelper.saveFavorite(placeId);
        } else {
            preferencesHelper.removeFavorite(placeId);
        }

        updateMarkers();
        Toast.makeText(
                this,
                place.isFavorite() ? R.string.favorite_added : R.string.favorite_removed,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    moveToCurrentLocation();
                }
            }
        }
    }

    // 메뉴바 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "위성 지도");
        menu.add(0, 2, 0, "일반 지도");
        menu.add(0, 3, 0, "경기도청 바로가기");
        return true;
    }

    // 메뉴바 클릭 시 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case 3:
                // 기본 위치를 경기도청으로 설정
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.2750, 127.0090), 13));
                return true;
        }
        return false;
    }
}