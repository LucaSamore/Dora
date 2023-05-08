package com.example.dora.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.dora.ui.navigation.DoraScreen
import com.example.dora.ui.navigation.NavigationGraph
import com.example.dora.ui.theme.DoraTheme
import com.example.dora.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isUserSignedIn = mainActivityViewModel.isUserSignedIn()

            setContent {
                DoraTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationGraph(
                            navController = rememberNavController(),
                            startDestination =
                                if (isUserSignedIn) DoraScreen.Home.name else DoraScreen.SignIn.name
                        )
                    }
                }
            }
        }
    }
}
