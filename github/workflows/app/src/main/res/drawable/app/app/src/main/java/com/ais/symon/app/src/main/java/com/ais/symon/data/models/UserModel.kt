package com.ais.symon.data.models

import com.google.firebase.auth.FirebaseUser

data class AppUser(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val isPremium: Boolean = false,
    val isBanned: Boolean = false,
    val isAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromFirebase(fbUser: FirebaseUser): AppUser {
            return AppUser(
                uid = fbUser.uid,
                email = fbUser.email ?: "",
                displayName = fbUser.displayName ?: fbUser.email?.substringBefore("@") ?: "User",
                createdAt = System.currentTimeMillis()
            )
        }
    }
}
