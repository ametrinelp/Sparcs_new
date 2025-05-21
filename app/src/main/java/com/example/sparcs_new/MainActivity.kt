package com.example.sparcs_new

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sparcs_new.ui.theme.theme.Sparcs_newTheme
import com.example.sparcs_new.viewModel.AppViewModelFactory
import com.example.sparcs_new.viewModel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            fun isNetworkAvailable(context: Context): Boolean {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = connectivityManager.activeNetwork
                val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

                return networkCapabilities != null &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }

            fun checkNetworkAndMakeRequest(context: Context) {
                if (!isNetworkAvailable(context)) {
                    Toast.makeText(context, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }


            val themeViewModel: ThemeViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()


            Sparcs_newTheme(darkTheme = isDarkTheme) {
                checkNetworkAndMakeRequest(this)
                SparcsApp()
            }
        }
    }
}
