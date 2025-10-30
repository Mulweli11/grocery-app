// File: com.example.ecome.utils/AccountManager.kt

package com.example.ecome.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Utility class to manage Firebase Authentication and user status.
 */
object AccountManager {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /**
     * @return The currently signed-in FirebaseUser, or null if no user is signed in.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * @return The display name or email of the current user, or a default string.
     */
    fun getUserIdentifier(): String {
        val user = getCurrentUser()
        return when {
            user?.displayName.isNullOrEmpty().not() -> user!!.displayName!!
            user?.email.isNullOrEmpty().not() -> user!!.email!!
            else -> "Guest User"
        }
    }

    fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    fun signOut() {
        auth.signOut()
    }
}