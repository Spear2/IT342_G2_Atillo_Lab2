package com.example.userauth.ui.screens.register

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
import com.example.userauth.ui.screens.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the repository
        authRepository = AuthRepository(AuthManager(this))

        val etEmail = findViewById<EditText>(R.id.etRegEmail)
        val etPassword = findViewById<EditText>(R.id.etRegPassword)
        val btnRegister = findViewById<Button>(R.id.btnSubmitRegister)
        val navigateToSignin = findViewById<TextView>(R.id.navigateToSignin)

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.text = "Creating account..."
            btnRegister.isEnabled = false




            // Make the network call in the background
            lifecycleScope.launch(Dispatchers.IO) {
                val isSuccess = authRepository.register(email, password)

                withContext(Dispatchers.Main) {
                    if (isSuccess) {
                        Toast.makeText(this@RegisterActivity, "Registration Successful! Please login.", Toast.LENGTH_LONG).show()
                        // Go back to the Login screen
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed. Email might be taken.", Toast.LENGTH_LONG).show()
                        btnRegister.text = "Sign Up"
                        btnRegister.isEnabled = true
                    }
                }
            }


        }
        navigateToSignin.setOnClickListener {
            // Create the "ticket" to go to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}