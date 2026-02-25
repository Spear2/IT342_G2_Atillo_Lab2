package com.example.userauth.data.repository

import android.util.Log
import com.example.userauth.data.api.AuthApiService
import com.example.userauth.data.dto.LoginRequestDTO
import com.example.userauth.data.dto.RegisterDTO
import com.example.userauth.security.AuthManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository(private val authManager: AuthManager) {

    // 1. Build Retrofit to point to your Spring Boot localhost (10.0.2.2 for emulator)
    private val retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8080/") for virtual emulator

        //for Physical Phone
        .baseUrl("http://192.168.254.101:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(AuthApiService::class.java)

    // 2. The actual login function
    suspend fun login(email: String, password: String): Boolean {
        return try {
            val request = LoginRequestDTO(email, password)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                authManager.saveToken(response.body()!!.token)
                true
            } else {
                // NEW: Print the exact HTTP error code and message!
                Log.e("AUTH_DEBUG", "Server rejected login: ${response.code()} - ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            // NEW: Print the exact crash/network error!
            Log.e("AUTH_DEBUG", "Network or Crash Error: ${e.message}", e)
            false
        }
    }

    // Add this right below your existing login() function!
    suspend fun register(email: String, password: String): Boolean {
        return try {
            // Using the RegisterDTO we created earlier
            val request = RegisterDTO(email, password)

            // Hit the Spring Boot /api/auth/register endpoint
            val response = apiService.register(request)

            // If Spring Boot returns 200 OK and a User object, it worked!
            if (response.isSuccessful && response.body() != null) {
                true
            } else {
                // NEW: Print the exact reason Spring Boot rejected it!
                Log.e("AUTH_DEBUG", "Register Failed: Code ${response.code()} - ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AUTH_DEBUG", "Network or Crash Error: ${e.message}", e)
            false
        }
    }
}