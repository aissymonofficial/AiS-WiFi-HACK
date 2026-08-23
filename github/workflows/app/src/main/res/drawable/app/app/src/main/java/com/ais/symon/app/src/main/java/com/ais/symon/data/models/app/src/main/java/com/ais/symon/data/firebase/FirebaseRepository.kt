package com.ais.symon.data.firebase

import android.util.Log
import com.ais.symon.data.models.AppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    companion object {
        private const val TAG = "FirebaseRepo"
        const val USERS_REF = "users"
        const val SETTINGS_REF = "settings"
        const val PAYMENTS_REF = "payments"
        const val PREMIUM_TOOLS_REF = "premium_tools"
        const val ADMIN_CONTACTS_REF = "admin_contacts"
    }

    // ========== AUTH ==========

    val currentUser: FirebaseAuth? get() = auth.currentUser?.let { auth }

    fun authStateFlow(): Flow<FirebaseAuth?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { authInstance ->
            trySend(authInstance.currentUser?.let { authInstance })
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun registerWithEmail(email: String, password: String, name: String): Result<AppUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val fbUser = result.user ?: return Result.failure(Exception("Registration failed"))

            // Update display name
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            fbUser.updateProfile(profileUpdate).await()

            // Save to database
            val appUser = AppUser(
                uid = fbUser.uid,
                email = fbUser.email ?: email,
                displayName = name,
                createdAt = System.currentTimeMillis()
            )
            db.child(USERS_REF).child(fbUser.uid).setValue(appUser).await()

            Result.success(appUser)
        } catch (e: Exception) {
            Log.e(TAG, "Registration error", e)
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<AppUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val fbUser = result.user ?: return Result.failure(Exception("Login failed"))
            Result.success(AppUser.fromFirebase(fbUser))
        } catch (e: Exception) {
            Log.e(TAG, "Login error", e)
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    fun getUserDetails(uid: String): Flow<AppUser?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(AppUser::class.java)
                trySend(user)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(null)
            }
        }
        db.child(USERS_REF).child(uid).addValueEventListener(listener)
        awaitClose { db.child(USERS_REF).child(uid).removeEventListener(listener) }
    }

    // ========== BAN CHECK ==========

    suspend fun isUserBanned(uid: String): Boolean {
        return try {
            val snapshot = db.child(USERS_REF).child(uid).child("isBanned").get().await()
            snapshot.getValue(Boolean::class.java) ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isAdmin(uid: String): Boolean {
        return try {
            val snapshot = db.child(USERS_REF).child(uid).child("isAdmin").get().await()
            snapshot.getValue(Boolean::class.java) ?: false
        } catch (e: Exception) {
            false
        }
    }

    // ========== ADMIN FUNCTIONS ==========

    suspend fun banUser(uid: String) {
        db.child(USERS_REF).child(uid).child("isBanned").setValue(true).await()
    }

    suspend fun unbanUser(uid: String) {
        db.child(USERS_REF).child(uid).child("isBanned").setValue(false).await()
    }

    suspend fun setPremium(toolName: String, premium: Boolean) {
        db.child(PREMIUM_TOOLS_REF).child(toolName).setValue(premium).await()
    }

    fun getAllUsersFlow(): Flow<List<AppUser>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(AppUser::class.java) }
                trySend(users)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        db.child(USERS_REF).addValueEventListener(listener)
        awaitClose { db.child(USERS_REF).removeEventListener(listener) }
    }

    suspend fun updateAdminContact(type: String, value: String) {
        db.child(ADMIN_CONTACTS_REF).child(type).setValue(value).await()
    }

    fun getAdminContactsFlow(): Flow<Map<String, String>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = mutableMapOf<String, String>()
                snapshot.children.forEach { child ->
                    child.getValue(String::class.java)?.let { map[child.key!!] = it }
                }
                trySend(map)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(emptyMap())
            }
        }
        db.child(ADMIN_CONTACTS_REF).addValueEventListener(listener)
        awaitClose { db.child(ADMIN_CONTACTS_REF).removeEventListener(listener) }
    }

    fun isToolPremiumFlow(toolName: String): Flow<Boolean> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(Boolean::class.java) ?: false)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(false)
            }
        }
        db.child(PREMIUM_TOOLS_REF).child(toolName).addValueEventListener(listener)
        awaitClose { db.child(PREMIUM_TOOLS_REF).child(toolName).removeEventListener(listener) }
    }
}

// Kotlin coroutine await extensions for Firebase Tasks
suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T {
    return kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
        addOnCanceledListener {
            continuation.cancel()
        }
    }
}
