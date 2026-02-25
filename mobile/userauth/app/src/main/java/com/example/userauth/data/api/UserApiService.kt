package com.example.userauth.data.api

import com.example.userauth.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApiService {

    // This perfectly matches your Spring Boot @GetMapping("/api/user/me")
    @GET("api/user/me")
    suspend fun getUserProfile(
        // The VIP Pass: We must attach "Bearer <Token>" to the header!
        @Header("Authorization") authHeader: String
    ): Response<User>

}