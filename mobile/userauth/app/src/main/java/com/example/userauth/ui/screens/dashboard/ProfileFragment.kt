package com.example.userauth.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.userauth.R
import com.example.userauth.data.repository.UserRepository
import com.example.userauth.security.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var authManager: AuthManager
    private lateinit var userRepository: UserRepository

    // 1. Inflate the XML layout you showed me earlier
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    // 2. Once the screen is built, fetch the data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our tools using the Activity's context
        authManager = AuthManager(requireContext())
        userRepository = UserRepository(authManager)

        val tvProfileEmail = view.findViewById<TextView>(R.id.tvProfileEmail)

        // Show a loading state temporarily
        tvProfileEmail.text = "Loading..."

        // 3. Fetch the data from Spring Boot in the background
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserProfile()

            // 4. Update the UI on the main thread
            withContext(Dispatchers.Main) {
                if (user != null) {
                    // Success! Display the email from the database
                    tvProfileEmail.text = user.email
                } else {
                    tvProfileEmail.text = "Error loading profile"
                    Toast.makeText(requireContext(), "Failed to fetch profile. Token might be expired.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}