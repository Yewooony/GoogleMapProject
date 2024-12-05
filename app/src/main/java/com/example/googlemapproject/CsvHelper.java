package com.example.googlemapproject;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {
    private final Context context;

    public CsvHelper(Context context) {
        this.context = context;
    }

    // 모든 장소 데이터를 로드하는 메서드
    public List<Place> loadAllPlaces() {
        List<Place> allPlaces = new ArrayList<>();

        // 각 PlaceType에 대해 CSV 파일을 로드
        for (PlaceType type : PlaceType.values()) {
            try {
                List<Place> places = loadPlacesFromRaw(type);
                allPlaces.addAll(places);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return allPlaces;
    }

    // raw 폴더에서 CSV 파일을 읽어 Place 객체 리스트로 변환하는 메서드
    private List<Place> loadPlacesFromRaw(PlaceType type) throws IOException {
        List<Place> places = new ArrayList<>();

        // 리소스 ID 가져오기
        int resourceId = context.getResources().getIdentifier(
                type.getFileName().replace(".csv", ""),
                "raw",
                context.getPackageName()
        );

        // 파일 읽기
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // 헤더 라인 건너뛰기
        reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] columns = line.split(",");
            if (columns.length >= 5) {
                try {
                    Place place = new Place(
                            columns[1].trim(),    // 이름
                            columns[0].trim(),    // 지역
                            columns[2].trim(),    // 주소
                            Double.parseDouble(columns[3].trim()), // 위도
                            Double.parseDouble(columns[4].trim()), // 경도
                            type
                    );
                    places.add(place);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        reader.close();
        return places;
    }
}