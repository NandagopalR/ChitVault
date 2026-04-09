package com.chitvault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.chitvault.app.ui.navigation.ChitVaultApp
import com.chitvault.app.ui.theme.ChitVaultTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChitVaultTheme {
                ChitVaultApp()
            }
        }
    }
}
