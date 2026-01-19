package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felixny.inkflow.InkFlow
import com.felixny.inkflow.InkFlowConfig
import com.felixny.inkflow.inkReveal
import kotlinx.coroutines.launch

@Composable
fun ButtonEffectScreen(
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val config = remember(isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet).copy(
            distortionStrength = 0.2f
        )
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Button Press Effects",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Add organic ink effects to button interactions",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Example buttons with different effects
            InteractiveButton(
                text = "Primary Action",
                config = config,
                modifier = Modifier.fillMaxWidth()
            )
            
            InteractiveButton(
                text = "Secondary Action",
                config = config,
                modifier = Modifier.fillMaxWidth()
            )
            
            InteractiveButton(
                text = "Destructive Action",
                config = config,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Examples")
            }
        }
    }
}

@Composable
fun InteractiveButton(
    text: String,
    config: InkFlowConfig,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isPressed by remember { mutableStateOf(false) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(animatableProgress.value) {
        progress = animatableProgress.value
    }
    
    Box(
        modifier = modifier
            .height(60.dp)
            .inkReveal(progress = progress, config = config),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (!isPressed) {
                    isPressed = true
                    coroutineScope.launch {
                        animatableProgress.animateTo(1f, tween(600))
                        kotlinx.coroutines.delay(200)
                        animatableProgress.animateTo(0f, tween(400))
                        isPressed = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF667eea)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isPressed
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
