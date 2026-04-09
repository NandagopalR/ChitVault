package com.chitvault.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PersonEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class ChitVaultDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}
