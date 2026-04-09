package com.chitvault.app.di

import android.content.Context
import androidx.room.Room
import com.chitvault.app.data.local.ChitVaultDatabase
import com.chitvault.app.data.local.PersonDao
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ChitVaultDatabase {
        return Room.databaseBuilder(
            context,
            ChitVaultDatabase::class.java,
            "chitvault.db",
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePersonDao(database: ChitVaultDatabase): PersonDao = database.personDao()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance("https://chitvault-35fcd-default-rtdb.firebaseio.com/")

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 },
            )
        }
}
