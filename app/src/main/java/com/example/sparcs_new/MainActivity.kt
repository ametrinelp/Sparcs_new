package com.example.sparcs_new

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sparcs_new.data.DataTokenStore
import com.example.sparcs_new.ui.theme.theme.Sparcs_newTheme
private lateinit var dataTokenStore: DataTokenStore
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Sparcs_newTheme {
                dataTokenStore = DataTokenStore(this)
                SparcsApp(dataTokenStore = dataTokenStore)
            }
        }
    }
}
