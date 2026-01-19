package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felixny.inkflow.InkFlow
import com.felixny.inkflow.InkFlowConfig
import com.felixny.inkflow.inkReveal
import kotlinx.coroutines.launch

@Composable
fun ProfileCardScreen(
    onBack: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Detect if device is a tablet
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    // Use InkFlow utility to create optimized config
    val config = remember(isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet)
    }
    
    // Sync animatable with state
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
                        Color(0xFF16213e),
                        Color(0xFF0f3460)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Profile Card with high-resolution background
            ProfileCard(
                progress = progress,
                config = config,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
            
            // Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress Slider
                Text(
                    text = "Progress: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
                Slider(
                    value = progress,
                    onValueChange = { newValue ->
                        progress = newValue
                        coroutineScope.launch {
                            animatableProgress.snapTo(newValue)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAnimating
                )
                
                // Send Button
                Button(
                    onClick = {
                        if (!isAnimating) {
                            isAnimating = true
                            coroutineScope.launch {
                                animatableProgress.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(
                                        durationMillis = 2000,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                                // Reset after animation
                                kotlinx.coroutines.delay(500)
                                animatableProgress.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(durationMillis = 300)
                                )
                                isAnimating = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAnimating
                ) {
                    Text(
                        text = if (isAnimating) "Sending..." else "Send",
                        style = MaterialTheme.typography.titleMedium
                    )
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
}

@Composable
fun ProfileCard(
    progress: Float,
    config: InkFlowConfig,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .inkReveal(progress = progress, config = config),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // High-resolution background image (using gradient as placeholder)
            // In a real app, you'd use a high-res image resource
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2),
                                Color(0xFFf093fb),
                                Color(0xFF4facfe)
                            ),
                            center = androidx.compose.ui.geometry.Offset(0f, 0f),
                            radius = 800f
                        )
                    )
            )
            
            // Content overlay
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Profile Avatar
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFff6b6b),
                                    Color(0xFFee5a6f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Name
                Text(
                    text = "Jane Doe",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Title
                Text(
                    text = "Senior Designer",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Bio
                Text(
                    text = "Creating beautiful experiences\none pixel at a time",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 24.sp
                )
            }
        }
    }
}
