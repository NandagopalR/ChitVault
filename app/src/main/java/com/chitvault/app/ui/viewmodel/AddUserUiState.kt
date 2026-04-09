package com.chitvault.app.ui.viewmodel

data class AddUserUiState(
    val name: String = "",
    val age: String = "",
    val mobile: String = "",
    val mail: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
)
