package com.chitvault.app.data.model

data class UserModel(
    val username: String = "",
    val mail: String = "",
    val age: Int = 0,
    val isAmountCredited: Boolean = false,
    val creditedDate: String = "",
    val creditedAmount: Double = 0.0,
)
