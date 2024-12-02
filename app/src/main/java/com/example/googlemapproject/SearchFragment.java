package com.example.googlemapproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<SearchItem> searchItems = new ArrayList<>();
    private SearchAdapter searchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);

        setupSearchView();
        setupRecyclerView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSearchView();
        setupRecyclerView();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 실시간 검색 구현 시 사용
                performSearch(newText);
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        searchAdapter = new SearchAdapter(searchItems,
                item -> Toast.makeText(requireContext(),
                        "선택된 항목: " + item.getName(), Toast.LENGTH_SHORT).show());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(searchAdapter);
    }

    private void performSearch(String query) {
        searchItems.clear();
        // 여기에 실제 검색 로직 구현
        searchItems.add(new SearchItem("검색 결과 1: " + query, "주소 1"));
        searchItems.add(new SearchItem("검색 결과 2: " + query, "주소 2"));
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        searchView = null;
        recyclerView = null;
        searchAdapter = null;
    }
}