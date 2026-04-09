package com.chitvault.app

import android.app.Application
import com.chitvault.app.data.remote.RemoteConfigRepository
import com.google.firebase.FirebaseApp
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class ChitVaultApplication : Application() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppEntryPoint {
        fun remoteConfigRepository(): RemoteConfigRepository
    }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val entryPoint = EntryPointAccessors.fromApplication(this, AppEntryPoint::class.java)
        applicationScope.launch {
            entryPoint.remoteConfigRepository().loadConfig()
        }
    }
}
