package com.example.midiariodeviajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import com.example.midiariodeviajes.ui.theme.MiDiarioDeViajesTheme
import com.example.midiariodeviajes.ui.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiDiarioDeViajesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WelcomeScreen()
                }
            }
        }
    }
}
