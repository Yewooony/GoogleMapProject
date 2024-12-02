package com.example.googlemapproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        setBottomNavigationView();

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new HomeFragment())
                    .commit();
        }
    }

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