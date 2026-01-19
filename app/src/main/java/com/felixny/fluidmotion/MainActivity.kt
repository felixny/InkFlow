package com.felixny.fluidmotion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.felixny.fluidmotion.examples.ButtonEffectScreen
import com.felixny.fluidmotion.examples.CardStackScreen
import com.felixny.fluidmotion.examples.ExampleScreen
import com.felixny.fluidmotion.examples.ExampleType
import com.felixny.fluidmotion.examples.ImageRevealScreen
import com.felixny.fluidmotion.examples.LoadingScreen
import com.felixny.fluidmotion.examples.ModalRevealScreen
import com.felixny.fluidmotion.examples.ProfileCardScreen
import com.felixny.fluidmotion.examples.TextRevealScreen
import com.felixny.fluidmotion.ui.theme.FluidMotionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FluidMotionTheme {
                InkFlowExamplesApp()
            }
        }
    }
}

@Composable
fun InkFlowExamplesApp() {
    var currentScreen by remember { mutableStateOf<Screen?>(null) }
    
    when (val screen = currentScreen) {
        null -> {
            ExampleScreen(
                onExampleSelected = { example ->
                    currentScreen = when (example) {
                        is ExampleType.ProfileCard -> Screen.ProfileCard
                        is ExampleType.ImageReveal -> Screen.ImageReveal
                        is ExampleType.TextReveal -> Screen.TextReveal
                        is ExampleType.ButtonEffect -> Screen.ButtonEffect
                        is ExampleType.LoadingState -> Screen.Loading
                        is ExampleType.CardStack -> Screen.CardStack
                        is ExampleType.ModalReveal -> Screen.Modal
                    }
                }
            )
        }
        is Screen.ProfileCard -> {
            ProfileCardScreen(
                onBack = { currentScreen = null }
            )
        }
        is Screen.ImageReveal -> {
            ImageRevealScreen(
                onBack = { currentScreen = null }
            )
        }
        is Screen.TextReveal -> {
            TextRevealScreen(
                onBack = { currentScreen = null }
            )
        }
        is Screen.ButtonEffect -> {
            ButtonEffectScreen(
                onBack = { currentScreen = null }
            )
        }
        is Screen.Loading -> {
            LoadingScreen(
                onBack = { currentScreen = null }
            )
        }
        is Screen.CardStack -> {
            CardStackScreen(
                onBack = { currentScreen = null }
            )
        }
        is Screen.Modal -> {
            ModalRevealScreen(
                onBack = { currentScreen = null }
            )
        }
    }
}

sealed class Screen {
    object ProfileCard : Screen()
    object ImageReveal : Screen()
    object TextReveal : Screen()
    object ButtonEffect : Screen()
    object Loading : Screen()
    object CardStack : Screen()
    object Modal : Screen()
}
