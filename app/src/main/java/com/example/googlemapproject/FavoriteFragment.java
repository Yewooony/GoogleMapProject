package com.example.googlemapproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<SearchItem> favoriteItems = new ArrayList<>();
    private SearchAdapter favoriteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView();
        loadFavorites();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadFavorites();
    }

    private void setupRecyclerView() {
        favoriteAdapter = new SearchAdapter(favoriteItems,
                item -> {
                    Toast.makeText(requireContext(),
                            "선택된 즐겨찾기: " + item.getName(),
                            Toast.LENGTH_SHORT).show();
                    // 여기에 즐겨찾기 아이템 클릭 시 처리할 로직 추가
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(favoriteAdapter);
    }

    private void loadFavorites() {
        favoriteItems.clear();
        // 여기에 실제 즐겨찾기 데이터를 불러오는 로직 구현
        favoriteItems.add(new SearchItem("즐겨찾기 1", "주소 1"));
        favoriteItems.add(new SearchItem("즐겨찾기 2", "주소 2"));
        favoriteItems.add(new SearchItem("즐겨찾기 3", "주소 3"));
        favoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
        favoriteAdapter = null;
    }
}