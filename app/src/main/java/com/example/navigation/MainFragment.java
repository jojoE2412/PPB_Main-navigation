package com.example.navigation;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewApiService newApiService;
    private final String API_KEY = "c0f9fc8e2fda44ef849184b268f424c2";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Animasi halus saat daftar muncul
        recyclerView.setAlpha(0f);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("User-Agent", "PortalIDApp")
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newApiService = retrofit.create(NewApiService.class);

        fetchNews(null, "indonesia");
    }

    public void fetchNews(String country, String query) {
        Call<NewResponse> call;
        if (query != null && !query.isEmpty()) {
            call = newApiService.getSearchNews(query, API_KEY);
        } else {
            call = newApiService.getTopHeadlines(country, API_KEY);
        }

        call.enqueue(new Callback<NewResponse>() {
            @Override
            public void onResponse(Call<NewResponse> call, Response<NewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<News> articles = response.body().getArticles();
                    if (articles != null && !articles.isEmpty()) {
                        NewsAdapter adapter = new NewsAdapter(articles, false);
                        recyclerView.setAdapter(adapter);
                        
                        // Jalankan animasi fade-in
                        recyclerView.animate().alpha(1f).setDuration(600).start();
                    } else {
                        Toast.makeText(getContext(), "Berita tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Code: " + response.code());
                    Toast.makeText(getContext(), "Gagal memuat berita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
