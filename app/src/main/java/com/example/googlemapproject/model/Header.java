package com.example.googlemapproject.model;

import com.google.gson.annotations.SerializedName;

// Header.java
public class Header {
    @SerializedName("LIST_TOTAL_COUNT")
    private int totalCount;

    @SerializedName("RESULT")
    private Result result;
}
