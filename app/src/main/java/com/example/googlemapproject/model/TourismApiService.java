// TourismResponse.java
package com.example.googlemapproject.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// TourismApiService.java
public interface TourismApiService {
    @GET("TOURESRTINFO")
    Call<TourismResponse> getTourismInfo(
            @Query("Key") String apiKey,
            @Query("Type") String type,
            @Query("pIndex") int pageIndex,
            @Query("pSize") int pageSize,
            @Query("SIGUN_NM") String sigunName
    );
}