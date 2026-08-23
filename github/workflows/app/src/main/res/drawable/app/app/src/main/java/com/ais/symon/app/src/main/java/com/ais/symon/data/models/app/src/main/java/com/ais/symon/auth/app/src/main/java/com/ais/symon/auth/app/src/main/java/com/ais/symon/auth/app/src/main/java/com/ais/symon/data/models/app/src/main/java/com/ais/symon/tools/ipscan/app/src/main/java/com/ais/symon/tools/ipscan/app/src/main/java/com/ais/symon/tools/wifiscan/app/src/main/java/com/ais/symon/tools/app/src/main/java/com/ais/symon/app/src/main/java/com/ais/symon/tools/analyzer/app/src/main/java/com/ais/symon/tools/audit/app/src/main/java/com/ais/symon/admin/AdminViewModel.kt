package com.ais.symon.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ais.symon.data.firebase.FirebaseRepository
import com.ais.symon.data.models.AppUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminState(
    val users: List<AppUser> = emptyList(),
    val isLoading: Boolean = false,
    val telegram: String = "",
    val whatsapp: String = "",
    val bKashNumber: String = "",
    val nagadNumber: String = "",
    val message: String? = null
)

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = FirebaseRepository()
    private val _state = MutableStateFlow(AdminState(isLoading = true))
    val state: StateFlow<AdminState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getAllUsersFlow().collect { users ->
                _state.update { it.copy(users = users, isLoading = false) }
            }
        }
        viewModelScope.launch {
            repo.getAdminContactsFlow().collect { contacts ->
                _state.update {
                    it.copy(
                        telegram = contacts["telegram"] ?: "",
                        whatsapp = contacts["whatsapp"] ?: "",
                        bKashNumber = contacts["bKash"] ?: "",
                        nagadNumber = contacts["nagad"] ?: ""
                    )
                }
            }
        }
    }

    fun banUser(uid: String) { viewModelScope.launch { repo.banUser(uid); _state.update { it.copy(message = "ইউজার ব্যান করা হয়েছে") } } }
    fun unbanUser(uid: String) { viewModelScope.launch { repo.unbanUser(uid); _state.update { it.copy(message = "ইউজার আনব্যান করা হয়েছে") } } }
    fun setPremium(tool: String, premium: Boolean) { viewModelScope.launch { repo.setPremium(tool, premium) } }
    
    fun updateTelegram(v: String) { viewModelScope.launch { repo.updateAdminContact("telegram", v); _state.update { it.copy(telegram = v) } } }
    fun updateWhatsApp(v: String) { viewModelScope.launch { repo.updateAdminContact("whatsapp", v); _state.update { it.copy(whatsapp = v) } } }
    fun updateBkash(v: String) { viewModelScope.launch { repo.updateAdminContact("bKash", v); _state.update { it.copy(bKashNumber = v) } } }
    fun updateNagad(v: String) { viewModelScope.launch { repo.updateAdminContact("nagad", v); _state.update { it.copy(nagadNumber = v) } } }
    
    fun clearMessage() { _state.update { it.copy(message = null) } }
}
