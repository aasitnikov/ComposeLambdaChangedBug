package com.example.composelambdachangedbug2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composelambdachangedbug2.hello.MyScreen
import com.example.composelambdachangedbug2.ui.theme.ComposeLambdaChangedBug2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLambdaChangedBug2Theme {
                MyScreen()
            }
        }
    }
}