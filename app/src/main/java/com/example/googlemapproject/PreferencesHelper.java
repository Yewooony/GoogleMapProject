package com.example.googlemapproject;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreferencesHelper {
    private final SharedPreferences prefs;
    private static final String PREFS_NAME = "MapAppPrefs";
    private static final String FAVORITE_PREFIX = "fav_";

    public PreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // 즐겨찾기 저장
    public void saveFavorite(String placeId) {
        prefs.edit()
                .putBoolean(FAVORITE_PREFIX + placeId, true)
                .apply();
    }

    // 즐겨찾기 제거
    public void removeFavorite(String placeId) {
        prefs.edit()
                .remove(FAVORITE_PREFIX + placeId)
                .apply();
    }

    // 즐겨찾기 여부 확인
    public boolean isFavorite(String placeId) {
        return prefs.getBoolean(FAVORITE_PREFIX + placeId, false);
    }

    // 모든 즐겨찾기 가져오기
    public Set<String> getAllFavorites() {
        Set<String> favorites = new HashSet<>();
        Map<String, ?> all = prefs.getAll();

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (entry.getKey().startsWith(FAVORITE_PREFIX) &&
                    entry.getValue() instanceof Boolean &&
                    (Boolean) entry.getValue()) {
                favorites.add(entry.getKey().substring(FAVORITE_PREFIX.length()));
            }
        }

        return favorites;
    }
}