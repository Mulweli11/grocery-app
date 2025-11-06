// File: com.example.ecome/AccountActivity.kt
package com.example.ecome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecome.utils.AccountManager
import com.example.ecome.utils.TranslationManager

class AccountActivity : AppCompatActivity() {

    private lateinit var currentUserIdentifierText: TextView
    private lateinit var signOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        currentUserIdentifierText = findViewById(R.id.currentUserIdentifierText)
        signOutButton = findViewById(R.id.signOutButton)

        // Optionally, set language here
        // TranslationManager.setLanguage(TranslationManager.Language.ZULU)

        displayUserInfo()
        setupSignOutButton()
    }

    private fun displayUserInfo() {
        // Update UI text dynamically based on selected language
        findViewById<TextView>(R.id.titleText).text = TranslationManager.getTranslation("title_account")
        findViewById<TextView>(R.id.nameLabel).text = TranslationManager.getTranslation("signed_in_as")
        signOutButton.text = TranslationManager.getTranslation("sign_out")

        val identifier = AccountManager.getUserIdentifier()
        currentUserIdentifierText.text = identifier

        // Hide sign-out if not logged in
        if (!AccountManager.isUserLoggedIn()) {
            signOutButton.visibility = android.view.View.GONE
            Toast.makeText(this, TranslationManager.getTranslation("guest_message"), Toast.LENGTH_LONG).show()
        }
    }

    private fun setupSignOutButton() {
        signOutButton.setOnClickListener {
            if (AccountManager.isUserLoggedIn()) {
                AccountManager.signOut()
                Toast.makeText(this, TranslationManager.getTranslation("signed_out"), Toast.LENGTH_SHORT).show()

                // Navigate to HomeActivity after sign-out
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, TranslationManager.getTranslation("already_signed_out"), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
