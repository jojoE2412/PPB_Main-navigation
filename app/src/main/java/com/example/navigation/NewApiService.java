package com.example.navigation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewApiService {
    // Cukup tulis endpoint-nya saja, karena /v2/ sudah ada di Base URL
    @GET("top-headlines")
    Call<NewResponse> getTopHeadlines(
        @Query("country") String country,
        @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<NewResponse> getSearchNews(
        @Query("q") String query,
        @Query("apiKey") String apiKey
    );
}
