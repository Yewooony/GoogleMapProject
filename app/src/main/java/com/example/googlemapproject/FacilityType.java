// FacilityType.java
package com.example.googlemapproject.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum FacilityType {
    ALL("전체"),
    NATURAL("자연관광지"),
    CULTURAL("문화관광지"),
    HISTORICAL("역사관광지"),
    RECREATIONAL("레포츠관광지"),
    OTHER("기타관광지");

    private final String displayName;

    FacilityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static FacilityType fromString(String text) {
        for (FacilityType type : FacilityType.values()) {
            if (type.displayName.equals(text)) {
                return type;
            }
        }
        return OTHER;
    }
}