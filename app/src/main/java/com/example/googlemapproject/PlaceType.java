package com.example.googlemapproject;

public enum PlaceType {
    ART("art.csv"),
    CAFE("cafe.csv"),
    GYM("gym.csv"),
    LIBRARY("library.csv"),
    RESTAURANT("restaurant.csv"),
    TOURIST("tourist.csv");

    private final String fileName;

    // 생성자
    PlaceType(String fileName) {
        this.fileName = fileName;
    }

    // 파일 이름으로부터 PlaceType을 찾는 메서드
    public static PlaceType fromFileName(String fileName) {
        for (PlaceType type : PlaceType.values()) {
            if (type.getFileName().equals(fileName)) {
                return type;
            }
        }
        return null;
    }

    public String getFileName() {
        return fileName;
    }
}