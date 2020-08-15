package com.example.boone.app3;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BackendDatabaseApi {

    @GET("books/")
    Call<List<Book>> getBooks(@Header("Authorization") String authHeader);

    @POST("books/")
    Call<Book> addBook(@Header("Authorization") String authHeader, @Body Book book);
}
