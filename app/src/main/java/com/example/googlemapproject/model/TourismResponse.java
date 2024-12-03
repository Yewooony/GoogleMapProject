package com.example.googlemapproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourismResponse {
    @SerializedName("TOURESRTINFO")
    private List<TourismData> tourismInfo;

    public List<TourismData> getTourismInfo() {
        return tourismInfo;
    }
}
