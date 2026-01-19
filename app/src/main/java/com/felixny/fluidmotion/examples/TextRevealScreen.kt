package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felixny.inkflow.InkFlow
import com.felixny.inkflow.InkFlowConfig
import com.felixny.inkflow.inkReveal
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun TextRevealScreen(
    onBack: () -> Unit
) {
    val texts = remember {
        listOf(
            "Welcome",
            "to",
            "InkFlow",
            "Beautiful animations",
            "made simple"
        )
    }
    
    // Create Animatable instances for each text
    val progress1 = remember { Animatable(0f) }
    val progress2 = remember { Animatable(0f) }
    val progress3 = remember { Animatable(0f) }
    val progress4 = remember { Animatable(0f) }
    val progress5 = remember { Animatable(0f) }
    
    val progresses = remember {
        listOf(progress1, progress2, progress3, progress4, progress5)
    }
    
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val config = remember(isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet).copy(
            edgeSoftness = 0.2f
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Text Reveal Animation",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reveal each text with staggered animation
            texts.forEachIndexed { index, text ->
                var progress by remember { mutableFloatStateOf(0f) }
                
                LaunchedEffect(progresses[index].value) {
                    progress = progresses[index].value
                }
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .inkReveal(progress = progress, config = config),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Reveal text elements sequentially with organic ink spread for dramatic effect.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Button(
                onClick = {
                    if (!isAnimating) {
                        isAnimating = true
                        coroutineScope.launch {
                            progresses.forEachIndexed { index, animatable ->
                                delay(index * 200L) // Staggered delay
                                animatable.animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(
                                        durationMillis = 1500,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                            delay(1000)
                            progresses.forEachIndexed { index, animatable ->
                                delay(index * 100L)
                                animatable.animateTo(0f, tween(300))
                            }
                            isAnimating = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isAnimating
            ) {
                Text(if (isAnimating) "Animating..." else "Animate Text Reveal")
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
