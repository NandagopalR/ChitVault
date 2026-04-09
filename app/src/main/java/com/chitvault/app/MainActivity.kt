package com.chitvault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.chitvault.app.data.local.SessionManager
import com.chitvault.app.ui.navigation.ChitVaultApp
import com.chitvault.app.ui.theme.ChitVaultTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChitVaultTheme {
                ChitVaultApp(isLoggedIn = sessionManager.isLoggedIn)
            }
        }
    }
}

