package com.chitvault.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chitvault.app.data.remote.RemoteConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            remoteConfigRepository.masterConfig.collect { config ->
                _uiState.update { it.copy(isConfigLoading = config == null) }
            }
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true) }
        val password = _uiState.value.password
        if (password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Password is required.") }
            return
        }

        val config = remoteConfigRepository.masterConfig.value
        if (config == null) {
            _uiState.update { it.copy(errorMessage = "Invalid credentials...") }
            return
        }
        val configMap = config.map
        if (configMap.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Invalid credentials...") }
            return
        }

        if (!configMap.containsKey(KEY_ADMIN)) {
            _uiState.update { it.copy(errorMessage = "Invalid credentials...") }
            return
        }

        val expectedPassword = config.map[KEY_ADMIN].orEmpty()

        if (password != expectedPassword) {
            _uiState.update { it.copy(errorMessage = "Invalid credentials...") }
            return
        }

        onSuccess()
    }

    companion object {
        private const val ADMIN_VALUE = "admin"
        private const val KEY_ADMIN = "admin"
        private const val KEY_PASSWORD = "password"
    }
}
