package com.felixny.fluidmotion.examples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CardStackScreen(
    onBack: () -> Unit
) {
    val cards = remember {
        listOf(
            "Card 1" to Color(0xFF667eea),
            "Card 2" to Color(0xFF764ba2),
            "Card 3" to Color(0xFFf093fb),
            "Card 4" to Color(0xFF4facfe)
        )
    }
    
    // Create Animatable instances for each card
    val progress1 = remember { Animatable(0f) }
    val progress2 = remember { Animatable(0f) }
    val progress3 = remember { Animatable(0f) }
    val progress4 = remember { Animatable(0f) }
    
    val progresses = remember {
        listOf(progress1, progress2, progress3, progress4)
    }
    
    var currentIndex by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val config = remember(isTablet) {
        InkFlow.createOptimizedConfig(isTablet = isTablet)
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
                text = "Card Stack Reveal",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stacked cards with reveal effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                cards.forEachIndexed { index, (title, color) ->
                    var progress by remember { mutableFloatStateOf(0f) }
                    
                    LaunchedEffect(progresses[index].value) {
                        progress = progresses[index].value
                    }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .offset(y = (index * 20).dp)
                            .inkReveal(progress = progress, config = config),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = color
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = (cards.size - index).dp * 2
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            Text(
                text = "Reveal stacked cards sequentially for a dramatic layered effect.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Button(
                onClick = {
                    if (!isAnimating) {
                        isAnimating = true
                        coroutineScope.launch {
                            // Reveal cards one by one
                            for (i in cards.indices) {
                                delay(i * 300L)
                                progresses[i].animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                            delay(1000)
                            // Hide cards in reverse
                            for (i in cards.indices.reversed()) {
                                delay(200L)
                                progresses[i].animateTo(0f, tween(400))
                            }
                            isAnimating = false
                            currentIndex = 0
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isAnimating
            ) {
                Text(if (isAnimating) "Revealing..." else "Reveal Card Stack")
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
