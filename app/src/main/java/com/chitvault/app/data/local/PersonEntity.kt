package com.chitvault.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chitvault.app.data.model.PersonModel

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey val id: String,
    val username: String,
    val mail: String,
    val age: Int,
    val isAmountCredited: Boolean,
    val creditedDate: String,
    val creditedAmount: Double,
)

fun PersonEntity.toModel(): PersonModel = PersonModel(
    id = id,
    username = username,
    mail = mail,
    age = age,
    isAmountCredited = isAmountCredited,
    creditedDate = creditedDate,
    creditedAmount = creditedAmount,
)

fun PersonModel.toEntity(): PersonEntity = PersonEntity(
    id = id.ifBlank { mail.ifBlank { username } },
    username = username,
    mail = mail,
    age = age,
    isAmountCredited = isAmountCredited,
    creditedDate = creditedDate,
    creditedAmount = creditedAmount,
)
