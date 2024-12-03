// MarkerManager.java
package com.example.googlemapproject.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.googlemapproject.model.TourismItem;
import com.example.googlemapproject.model.FacilityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class MarkerManager {
    private final GoogleMap map;
    private final Map<FacilityType, List<Marker>> markersByType;
    private final Map<Marker, com.example.googlemapproject.model.TourismItem> markerData;

    public MarkerManager(GoogleMap map) {
        this.map = map;
        this.markersByType = new HashMap<>();
        this.markerData = new HashMap<>();

        // 각 시설 유형별 리스트 초기화
        for (FacilityType type : FacilityType.values()) {
            markersByType.put(type, new ArrayList<>());
        }
    }

    public void addMarker(com.example.googlemapproject.model.TourismItem item) {
        try {
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(item.getLatitude(), item.getLongitude()))
                    .title(item.getFacilityName())
                    .snippet(item.getAddress());

            Marker marker = map.addMarker(options);
            FacilityType type = FacilityType.fromString(item.getFacilityType());

            markersByType.get(type).add(marker);
            markerData.put(marker, item);
        } catch (NumberFormatException e) {
            // 위도/경도 파싱 오류 처리
        }
    }

    public void showMarkersOfType(FacilityType type) {
        // 모든 마커 숨기기
        hideAllMarkers();

        // 선택된 유형이 '전체'이면 모든 마커 표시
        if (type == FacilityType.ALL) {
            showAllMarkers();
            return;
        }

        // 선택된 유형의 마커만 표시
        List<Marker> markers = markersByType.get(type);
        for (Marker marker : markers) {
            marker.setVisible(true);
        }
    }

    private void hideAllMarkers() {
        for (List<Marker> markers : markersByType.values()) {
            for (Marker marker : markers) {
                marker.setVisible(false);
            }
        }
    }

    private void showAllMarkers() {
        for (List<Marker> markers : markersByType.values()) {
            for (Marker marker : markers) {
                marker.setVisible(true);
            }
        }
    }

    public com.example.googlemapproject.model.TourismItem getItemForMarker(Marker marker) {
        return markerData.get(marker);
    }

    public void clearMarkers() {
        for (List<Marker> markers : markersByType.values()) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
        markerData.clear();
    }
}