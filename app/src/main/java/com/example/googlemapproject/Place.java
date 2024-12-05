package com.example.googlemapproject;

public class Place {
    private String name;        // 장소 이름
    private String region;      // 지역
    private String address;     // 주소
    private double latitude;    // 위도
    private double longitude;   // 경도
    private PlaceType type;     // 장소 유형
    private boolean isFavorite; // 즐겨찾기 여부

    // 생성자
    public Place(String name, String region, String address,
                 double latitude, double longitude, PlaceType type) {
        this.name = name;
        this.region = region;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.isFavorite = false;
    }

    // Getter와 Setter 메서드들
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public PlaceType getType() {
        return type;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}