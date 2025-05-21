package com.example.sparcs_new

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sparcs_new.data.DataTokenStore
import com.example.sparcs_new.ui.theme.theme.Sparcs_newTheme
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.viewModel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            Sparcs_newTheme(darkTheme = isDarkTheme) {
                SparcsApp()
            }
        }
    }
}
