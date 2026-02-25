package com.example.userauth.ui.screens.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.userauth.R
import com.example.userauth.data.repository.AuthRepository
import com.example.userauth.security.AuthManager
import com.example.userauth.ui.screens.dashboard.DashboardActivity
import com.example.userauth.ui.screens.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    // These will hold our tools
    private lateinit var authManager: AuthManager
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Initialize the Security Vault and Network Manager
        authManager = AuthManager(this)
        authRepository = AuthRepository(authManager)

        // 2. Find the input fields and button from the XML
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSubmitLogin = findViewById<Button>(R.id.btnSubmitLogin)
        val navigateToSignup = findViewById<TextView>(R.id.navigateToSignup)

        // 3. What happens when the user clicks "Sign In"?
        btnSubmitLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Basic check: Are the fields empty?
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Stop running the code here
            }

            // Visual feedback: Change the button text and disable it so they don't spam click
            btnSubmitLogin.text = "Logging in..."
            btnSubmitLogin.isEnabled = false



            // 4. THE MAGIC: Open a background thread to talk to Spring Boot
            lifecycleScope.launch(Dispatchers.IO) {

                // This line actually hits http://10.0.2.2:8080/api/auth/login
                val isSuccess = authRepository.login(email, password)

                // 5. Switch back to the Main UI Thread to update the screen
                withContext(Dispatchers.Main) {
                    if (isSuccess) {
                        // Show a little pop-up message
                        Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to the Dashboard
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)

                        // Close the Login screen entirely so they can't press 'Back' to return to it
                        finish()
                    } else {
                        // Failed! Reset the button and show an error
                        Toast.makeText(this@LoginActivity, "Login Failed. Check credentials.", Toast.LENGTH_LONG).show()
                        btnSubmitLogin.text = "Sign In"
                        btnSubmitLogin.isEnabled = true
                    }
                }
            }


        }
        navigateToSignup.setOnClickListener {
            // Create the "ticket" to go to LoginActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}