package com.mobileapllication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


//This interface contains all web service definitions for fetching books data from Google Books API

public interface BookService {

    @GET("volumes")
    Call<BooksResponse> searchBooks(@Query("q") String query, @Query("key") String apiKey, @Query("maxResults") int maxResults);

}
