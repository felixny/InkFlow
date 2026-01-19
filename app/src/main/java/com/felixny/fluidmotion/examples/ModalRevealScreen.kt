package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun ModalRevealScreen(
    onBack: () -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val config = remember(isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet).copy(
            edgeSoftness = 0.2f
        )
    }
    
    // Sync animatable with state
    LaunchedEffect(animatableProgress.value) {
        progress = animatableProgress.value
    }
    
    // Animate modal reveal when shown
    LaunchedEffect(showModal) {
        if (showModal) {
            // Reset to 0 first
            animatableProgress.snapTo(0f)
            progress = 0f
            // Small delay to ensure modal is rendered
            kotlinx.coroutines.delay(50)
            // Animate to 1
            animatableProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
        } else {
            // Reset when hidden
            animatableProgress.snapTo(0f)
            progress = 0f
        }
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
                text = "Modal Dialog Reveal",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Animate modal dialogs and bottom sheets with organic ink reveal",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Button(
                onClick = {
                    showModal = true
                    // Animation will be triggered by LaunchedEffect
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Modal")
            }
            
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Examples")
            }
        }
        
        // Modal overlay
        if (showModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                animatableProgress.animateTo(0f, tween(400))
                                kotlinx.coroutines.delay(400)
                                showModal = false
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(
                            onClick = { /* Prevent click from propagating to overlay */ }
                        )
                        .inkReveal(progress = progress, config = config),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    // Add colorful gradient background to make ink reveal more visible
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF667eea),
                                        Color(0xFF764ba2),
                                        Color(0xFFf093fb)
                                    ),
                                    center = androidx.compose.ui.geometry.Offset(0f, 0f),
                                    radius = 800f
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Modal Dialog",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            Text(
                                text = "This modal appears with an organic ink reveal effect. Perfect for dialogs, bottom sheets, and overlays.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        animatableProgress.animateTo(0f, tween(400))
                                        kotlinx.coroutines.delay(400)
                                        showModal = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }
}
