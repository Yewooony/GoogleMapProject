package com.example.googlemapproject.model;

import com.google.gson.annotations.SerializedName;

// TourismItem.java
public class TourismItem {
    @SerializedName("FACLT_NM")
    private String facilityName;

    @SerializedName("REFINE_WGS84_LAT")
    private String latitude;

    @SerializedName("REFINE_WGS84_LOGT")
    private String longitude;

    @SerializedName("REFINE_LOTNO_ADDR")
    private String address;

    @SerializedName("FACLT_DIV_NM")
    private String facilityType;

    @SerializedName("TOURESRT_INFO")
    private String description;

    // Getters
    public String getFacilityName() {
        return facilityName;
    }

    public double getLatitude() {
        return Double.parseDouble(latitude);
    }

    public double getLongitude() {
        return Double.parseDouble(longitude);
    }

    public String getAddress() {
        return address;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public String getDescription() {
        return description;
    }
}
