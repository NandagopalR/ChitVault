package com.chitvault.app.data.model

data class PersonModel(
    val id: String = "",
    val username: String = "",
    val mail: String = "",
    val mobile: String = "",
    val age: Int = 0,
    val isAmountCredited: Boolean = false,
    val creditedDate: String = "",
    val creditedAmount: Double = 0.0,
)
