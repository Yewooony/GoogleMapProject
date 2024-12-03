package com.example.googlemapproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.googlemapproject.model.FacilityType;
import com.example.googlemapproject.model.TourismItem;
import com.example.googlemapproject.utils.MarkerManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MarkerManager markerManager;
    private Spinner facilityTypeSpinner;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu in fragment
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 기존 레이아웃을 수정하여 스피너를 추가
        View view = inflater.inflate(R.layout.home, container, false);

        // 스피너 초기화
        facilityTypeSpinner = view.findViewById(R.id.facility_type_spinner);
        setupFacilityTypeSpinner();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    private void setupFacilityTypeSpinner() {
        // 스피너 어댑터 설정
        ArrayList<String> facilityTypes = new ArrayList<>();
        for (FacilityType type : FacilityType.values()) {
            facilityTypes.add(type.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                facilityTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facilityTypeSpinner.setAdapter(adapter);

        // 스피너 선택 이벤트 처리
        facilityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                FacilityType type = FacilityType.fromString(selectedType);
                if (markerManager != null) {
                    markerManager.showMarkersOfType(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (markerManager != null) {
                    markerManager.showMarkersOfType(FacilityType.ALL);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            }, LOCATION_PERMISSION_REQUEST_CODE);
//            return;
//        }
//        enableMyLocation();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemNone) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        } else if (itemId == R.id.itemNormal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (itemId == R.id.itemHybrid) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (itemId == R.id.itemSatellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (itemId == R.id.itemTerrain) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMarkersToMap(List<TourismItem> items) {
        markerManager.clearMarkers();
        for (TourismItem item : items) {
            markerManager.addMarker(item);
        }

        // 마커 클릭 이벤트 처리
        mMap.setOnMarkerClickListener(marker -> {
            TourismItem item = markerManager.getItemForMarker(marker);
            if (item != null) {
                showTourismDetails(item);
            }
            return false;
        });
    }
    private void showTourismDetails(TourismItem item) {
        // MaterialAlertDialogBuilder를 사용하여 더 현대적인 디자인의 다이얼로그를 만듭니다
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(item.getFacilityName())
                .setMessage(createDetailMessage(item))
                .setPositiveButton("확인", null)
                .setNeutralButton("길찾기", (dialog, which) -> {
                    // 구글 맵으로 길찾기 인텐트를 실행합니다
                    Uri gmmIntentUri = Uri.parse(String.format(Locale.KOREA,
                            "google.navigation:q=%f,%f&mode=d",
                            item.getLatitude(),
                            item.getLongitude()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    // 구글 맵 앱이 설치되어 있는지 확인합니다
                    if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(requireContext(),
                                "구글 맵 앱이 설치되어 있지 않습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    // 관광지 상세 정보 메시지를 생성하는 헬퍼 메서드입니다
    private String createDetailMessage(TourismItem item) {
        StringBuilder message = new StringBuilder();
        message.append("시설구분: ").append(item.getFacilityType()).append("\n\n");
        message.append("주소: ").append(item.getAddress()).append("\n\n");

        if (item.getDescription() != null && !item.getDescription().isEmpty()) {
            message.append("설명:\n").append(item.getDescription()).append("\n\n");
        }

        return message.toString();
    }
}