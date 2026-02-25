package com.example.userauth.ui.screens.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.userauth.R
import com.example.userauth.security.AuthManager
import com.example.userauth.ui.screens.landing.LandingActivity
import com.example.userauth.ui.screens.login.LoginActivity
import com.example.userauth.ui.screens.profile.ProfileFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var authManager: AuthManager
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        authManager = AuthManager(this)

        // Find the Drawer and Menu buttons
        drawerLayout = findViewById(R.id.drawerLayout)
        val navDashboard = findViewById<TextView>(R.id.navDashboard)
        val navProfile = findViewById<TextView>(R.id.navProfile)
        val btnLogout = findViewById<TextView>(R.id.btnLogout)

        // Automatically load the Profile Fragment when the Dashboard first opens
        // (You can create a separate DashboardFragment later if you want!)
        // 1. Automatically load the Dashboard Fragment when they first log in
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        // --- CLICK LISTENERS FOR THE MENU (The Router) ---

        navDashboard.setOnClickListener {
            loadFragment(DashboardFragment()) // Swap to Dashboard
            drawerLayout.closeDrawer(GravityCompat.START) // Close the menu
        }

        navProfile.setOnClickListener {
            loadFragment(ProfileFragment()) // Swap to Profile
            drawerLayout.closeDrawer(GravityCompat.START) // Close the menu
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    // Helper function to swap out the fragments in the middle of the screen
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun logoutUser() {
        authManager.clearToken()
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }
}