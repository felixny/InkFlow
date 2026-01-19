package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import com.felixny.inkflow.inkReveal
import kotlinx.coroutines.launch

@Composable
fun LoadingScreen(
    onBack: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isLoading by remember { mutableStateOf(false) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val config = remember(isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet).copy(
            edgeSoftness = 0.1f
        )
    }
    
    LaunchedEffect(animatableProgress.value) {
        progress = animatableProgress.value
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
                text = "Loading State",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Loading card that reveals as progress increases
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .inkReveal(progress = progress, config = config),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(bottom = 16.dp),
                            color = Color(0xFF667eea)
                        )
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF667eea),
                        )
                    } else {
                        Text(
                            text = "Ready to load",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                }
            }
            
            Text(
                text = "Use ink reveal to show loading progress with an organic, fluid animation.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Button(
                onClick = {
                    if (!isLoading) {
                        isLoading = true
                        coroutineScope.launch {
                            animatableProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 3000,
                                    easing = LinearEasing
                                )
                            )
                            kotlinx.coroutines.delay(500)
                            isLoading = false
                            animatableProgress.animateTo(0f, tween(300))
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Loading..." else "Start Loading")
            }
            
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Examples")
            }
        }
    }
}
