package com.ais.symon.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ais.symon.data.firebase.FirebaseRepository
import com.ais.symon.data.models.AppUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: AppUser? = null,
    val error: String? = null,
    val isAdmin: Boolean = false,
    val isBanned: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            val fbUser = repository.currentUser?.currentUser
            if (fbUser != null) {
                _state.update { it.copy(isLoading = true) }
                repository.getUserDetails(fbUser.uid).collect { appUser ->
                    if (appUser != null) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                user = appUser,
                                isAdmin = appUser.isAdmin,
                                isBanned = appUser.isBanned
                            )
                        }
                    }
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = repository.registerWithEmail(email, password, name)
            result.fold(
                onSuccess = { user ->
                    _state.update {
                        it.copy(isLoading = false, isLoggedIn = true, user = user)
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Registration failed")
                    }
                }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = repository.loginWithEmail(email, password)
            result.fold(
                onSuccess = { user ->
                    // Check ban status
                    val banned = repository.isUserBanned(user.uid)
                    if (banned) {
                        repository.signOut()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoggedIn = false,
                                error = "❌ আপনার অ্যাকাউন্ট ব্লক করা হয়েছে। প্রশাসকের সাথে যোগাযোগ করুন।"
                            )
                        }
                    } else {
                        val isAdmin = repository.isAdmin(user.uid)
                        _state.update {
                            it.copy(isLoading = false, isLoggedIn = true, user = user, isAdmin = isAdmin)
                        }
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, error = error.message ?: "Login failed")
                    }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.signOut()
            _state.update { AuthState() }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
