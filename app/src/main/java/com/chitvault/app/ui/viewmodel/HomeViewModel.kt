package com.chitvault.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chitvault.app.data.remote.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FirebaseRepository,
) : ViewModel() {

    private val syncState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = combine(
        syncState,
        repository.observePersons(),
    ) { sync, persons ->
        sync.copy(persons = persons.sortedBy { it.isAmountCredited })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            syncState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.syncPersons()
                .onSuccess {
                    syncState.update { it.copy(isLoading = false, errorMessage = null) }
                }
                .onFailure { throwable ->
                    syncState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to fetch persons from Firebase.",
                        )
                    }
                }
        }
    }
}
