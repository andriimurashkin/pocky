package com.amur.pocky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.amur.pocky.ui.navigation.PockyNavGraph
import com.amur.pocky.ui.theme.PockyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PockyTheme {
                val navController = rememberNavController()
                PockyNavGraph(navController = navController)
            }
        }
    }
}
