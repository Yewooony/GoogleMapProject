package com.example.googlemapproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 툴바 설정 설정
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_gmap);
        getSupportActionBar().setTitle("YW's GoogleMap App");

        // 바텀 네비바 설정
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        setBottomNavigationView();

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new HomeFragment())
                    .commit();
        }
    }

    // 바텀네비게이션 바
    private void setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new HomeFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.nav_search) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new SearchFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.nav_favorite) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new FavoriteFragment())
                        .commit();
                return true;
            }
            return false;
        });
    }
}