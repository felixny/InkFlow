package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun FlowControlScreen(
    onBack: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val animatableProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Speed control
    var speedMultiplier by remember { mutableFloatStateOf(1.0f) }
    
    // Center position control
    var selectedPosition by remember { mutableStateOf<Position>(Position.CENTER) }
    
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    val config = remember(speedMultiplier, selectedPosition, isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet).copy(
            speedMultiplier = speedMultiplier,
            centerX = selectedPosition.centerX,
            centerY = selectedPosition.centerY
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Flow Control",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Test speed multiplier and flow origin position",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Preview Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .inkReveal(progress = progress, config = config),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
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
                                center = androidx.compose.ui.geometry.Offset(400f, 300f),
                                radius = 600f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Preview",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            // Speed Multiplier Control
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Speed Multiplier: ${String.format("%.1f", speedMultiplier)}x",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Controls how fast the ink spreads visually",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Slider(
                        value = speedMultiplier,
                        onValueChange = { speedMultiplier = it },
                        valueRange = 0.1f..3.0f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "0.1x (Slow)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "1.0x (Normal)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "3.0x (Fast)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            // Center Position Control
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Flow Origin Position",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Where the ink flow starts from",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    
                    // Position grid
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Top row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PositionButton(
                                position = Position.TOP_LEFT,
                                selected = selectedPosition == Position.TOP_LEFT,
                                onClick = { selectedPosition = Position.TOP_LEFT },
                                modifier = Modifier.weight(1f)
                            )
                            PositionButton(
                                position = Position.TOP_CENTER,
                                selected = selectedPosition == Position.TOP_CENTER,
                                onClick = { selectedPosition = Position.TOP_CENTER },
                                modifier = Modifier.weight(1f)
                            )
                            PositionButton(
                                position = Position.TOP_RIGHT,
                                selected = selectedPosition == Position.TOP_RIGHT,
                                onClick = { selectedPosition = Position.TOP_RIGHT },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Middle row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PositionButton(
                                position = Position.LEFT_CENTER,
                                selected = selectedPosition == Position.LEFT_CENTER,
                                onClick = { selectedPosition = Position.LEFT_CENTER },
                                modifier = Modifier.weight(1f)
                            )
                            PositionButton(
                                position = Position.CENTER,
                                selected = selectedPosition == Position.CENTER,
                                onClick = { selectedPosition = Position.CENTER },
                                modifier = Modifier.weight(1f)
                            )
                            PositionButton(
                                position = Position.RIGHT_CENTER,
                                selected = selectedPosition == Position.RIGHT_CENTER,
                                onClick = { selectedPosition = Position.RIGHT_CENTER },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Bottom row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PositionButton(
                                position = Position.BOTTOM_LEFT,
                                selected = selectedPosition == Position.BOTTOM_LEFT,
                                onClick = { selectedPosition = Position.BOTTOM_LEFT },
                                modifier = Modifier.weight(1f)
                            )
                            PositionButton(
                                position = Position.BOTTOM_CENTER,
                                selected = selectedPosition == Position.BOTTOM_CENTER,
                                onClick = { selectedPosition = Position.BOTTOM_CENTER },
                                modifier = Modifier.weight(1f)
                            )
                            PositionButton(
                                position = Position.BOTTOM_RIGHT,
                                selected = selectedPosition == Position.BOTTOM_RIGHT,
                                onClick = { selectedPosition = Position.BOTTOM_RIGHT },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            
            // Animate Button
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
                Text(if (isAnimating) "Animating..." else "Animate Preview")
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

@Composable
fun PositionButton(
    position: Position,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
            containerColor = if (selected) {
                Color(0xFF667eea)
            } else {
                Color.White.copy(alpha = 0.1f)
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = position.label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color.White else Color.White.copy(alpha = 0.8f)
        )
    }
}

enum class Position(
    val label: String,
    val centerX: Float,
    val centerY: Float
) {
    TOP_LEFT("TL", 0.0f, 0.0f),
    TOP_CENTER("TC", 0.5f, 0.0f),
    TOP_RIGHT("TR", 1.0f, 0.0f),
    LEFT_CENTER("LC", 0.0f, 0.5f),
    CENTER("C", 0.5f, 0.5f),
    RIGHT_CENTER("RC", 1.0f, 0.5f),
    BOTTOM_LEFT("BL", 0.0f, 1.0f),
    BOTTOM_CENTER("BC", 0.5f, 1.0f),
    BOTTOM_RIGHT("BR", 1.0f, 1.0f)
}
