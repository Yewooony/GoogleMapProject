package com.example.googlemapproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// TourismData.java
public class TourismData {
    @SerializedName("head")
    private List<Header> header;

    @SerializedName("row")
    private List<TourismItem> items;

    public List<TourismItem> getItems() {
        return items;
    }
}
