package com.pcjr.service;

import com.pcjr.model.Product;
import com.pcjr.model.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Mario on 2016/5/4.
 */
public interface ApiService {
    @GET("/user")
    Call<Product> loadPro();
    @GET("/users/{user}")
    Call<Users> loadUsers(@Path("user") String user);
}
