package com.chitvault.app.ui.viewmodel

import com.chitvault.app.data.model.PersonModel

data class HomeUiState(
    val isLoading: Boolean = true,
    val persons: List<PersonModel> = emptyList(),
    val errorMessage: String? = null,
)
