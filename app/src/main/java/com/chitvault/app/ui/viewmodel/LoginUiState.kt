package com.chitvault.app.ui.viewmodel

data class LoginUiState(
    val password: String = "",
    val isLoading: Boolean = false,
    val isConfigLoading: Boolean = true,
    val errorMessage: String? = null,
)
