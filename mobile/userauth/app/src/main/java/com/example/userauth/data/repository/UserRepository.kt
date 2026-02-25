package com.example.userauth.data.repository

import com.example.userauth.data.api.UserApiService
import com.example.userauth.data.model.User
import com.example.userauth.security.AuthManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository(private val authManager: AuthManager) {

    // Same setup as before - keep your physical phone IP here!
    private val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8080/") for virtual emulator

        //for Physical Phone
        .baseUrl("http://192.168.254.101:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(UserApiService::class.java)

    // Fetch the profile using the saved token
    suspend fun getUserProfile(): User? {
        // 1. Grab the token from the vault
        val token = authManager.getToken() ?: return null

        return try {
            // 2. Pass the token to Spring Boot! (Notice the "Bearer " prefix)
            val response = apiService.getUserProfile("Bearer $token")

            if (response.isSuccessful) {
                response.body() // Returns the User object
            } else {
                null // Token might be expired
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}