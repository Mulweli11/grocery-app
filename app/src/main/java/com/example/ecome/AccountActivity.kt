// File: com.example.ecome/AccountActivity.kt

package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecome.utils.AccountManager // Assumes you created the AccountManager file

class AccountActivity : AppCompatActivity() {

    private lateinit var currentUserIdentifierText: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        currentUserIdentifierText = findViewById(R.id.currentUserIdentifierText)
        signOutButton = findViewById(R.id.signOutButton)

        displayUserInfo()
        setupSignOutButton()
    }

    private fun displayUserInfo() {
        val identifier = AccountManager.getUserIdentifier()
        currentUserIdentifierText.text = identifier

        // Hide the sign out button if the user is not actually logged in (e.g., Guest User)
        if (!AccountManager.isUserLoggedIn()) {
            signOutButton.visibility = android.view.View.GONE
            Toast.makeText(this, "You are viewing this as a guest.", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupSignOutButton() {
        signOutButton.setOnClickListener {
            if (AccountManager.isUserLoggedIn()) {
                AccountManager.signOut()
                Toast.makeText(this, "Successfully signed out.", Toast.LENGTH_SHORT).show()

                // IMPORTANT: Navigate the user back to the login or home screen
                val intent = Intent(this, HomeActivity::class.java) // Assuming HomeActivity is your main hub
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "You are already signed out.", Toast.LENGTH_SHORT).show()
                finish() // Close the account screen
            }
        }
    }
}