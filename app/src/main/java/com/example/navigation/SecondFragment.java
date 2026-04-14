package com.example.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SecondFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private TextView emptyText;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_bookmark);
        emptyText = view.findViewById(R.id.text_empty_bookmark);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateBookmarks();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBookmarks();
    }

    private void updateBookmarks() {
        if (getContext() != null) {
            List<News> bookmarkedList = BookmarkManager.getBookmarks(getContext());
            if (bookmarkedList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
                
                // Animasi halus saat daftar muncul
                recyclerView.setAlpha(0f);
                
                adapter = new NewsAdapter(bookmarkedList, true);
                recyclerView.setAdapter(adapter);
                
                // Jalankan animasi fade-in
                recyclerView.animate().alpha(1f).setDuration(600).start();
            }
        }
    }
}
