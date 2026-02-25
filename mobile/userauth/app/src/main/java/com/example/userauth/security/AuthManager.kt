package com.example.userauth.security

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {

    // This is Android's version of React's "localStorage"
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}