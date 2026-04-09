package com.chitvault.app.data.remote

import com.chitvault.app.data.model.MasterConfigModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {
    private val _masterConfig = MutableStateFlow<MasterConfigModel?>(null)
    val masterConfig: StateFlow<MasterConfigModel?> = _masterConfig.asStateFlow()

    suspend fun loadConfig() {
        remoteConfig.fetchAndActivate().await()
        val map = remoteConfig.all.mapValues { it.value.asString() }
        _masterConfig.value = MasterConfigModel(map)
    }

    fun getString(key: String): String = remoteConfig.getString(key)

    fun getBoolean(key: String): Boolean = remoteConfig.getBoolean(key)

    fun getLong(key: String): Long = remoteConfig.getLong(key)

    fun getDouble(key: String): Double = remoteConfig.getDouble(key)
}
