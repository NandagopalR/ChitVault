package com.chitvault.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chitvault.app.data.model.PersonModel
import com.chitvault.app.data.remote.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val repository: FirebaseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddUserUiState())
    val uiState: StateFlow<AddUserUiState> = _uiState.asStateFlow()

    fun updateName(value: String) = _uiState.update { it.copy(name = value, errorMessage = null) }
    fun updateAge(value: String) = _uiState.update { it.copy(age = value, errorMessage = null) }
    fun updateMobile(value: String) = _uiState.update { it.copy(mobile = value, errorMessage = null) }
    fun updateMail(value: String) = _uiState.update { it.copy(mail = value, errorMessage = null) }

    fun createPerson() {
        val state = _uiState.value
        val validationError = validate(state)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val person = PersonModel(
                username = state.name.trim(),
                mail = state.mail.trim(),
                mobile = state.mobile.trim(),
                age = state.age.trim().toIntOrNull() ?: 0,
            )
            repository.addPerson(person)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSaved = true) }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Failed to save. Please try again.",
                        )
                    }
                }
        }
    }

    private fun validate(state: AddUserUiState): String? {
        if (state.name.isBlank()) return "Name is required."
        if (state.age.isBlank()) return "Age is required."
        if (state.age.toIntOrNull() == null || state.age.toInt() <= 0) return "Enter a valid age."
        if (state.mobile.isBlank()) return "Mobile number is required."
        if (state.mobile.length < 10) return "Enter a valid mobile number."
        return null
    }
}
