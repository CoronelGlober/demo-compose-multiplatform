package com.david.remote.server.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import com.demo.domain.screens.optionsscreen.MainScreenViewModel
import com.demo.ui.composables.MainScreen

class MainActivity : AppCompatActivity() {
    val vm: MainScreenViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen(vm)
            }
        }
    }
}