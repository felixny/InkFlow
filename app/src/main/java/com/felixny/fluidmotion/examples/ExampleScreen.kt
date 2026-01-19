package com.felixny.fluidmotion.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felixny.inkflow.InkFlow

sealed class ExampleType(val title: String, val description: String) {
    object ProfileCard : ExampleType("Profile Card", "Reveal profile information with ink bleed")
    object ImageReveal : ExampleType("Image Reveal", "Animate image appearance with organic spread")
    object TextReveal : ExampleType("Text Animation", "Reveal text with fluid motion")
    object ButtonEffect : ExampleType("Button Press", "Interactive button with ink effect")
    object LoadingState : ExampleType("Loading State", "Show loading progress with ink reveal")
    object CardStack : ExampleType("Card Stack", "Reveal stacked cards one by one")
    object ModalReveal : ExampleType("Modal Dialog", "Animate modal appearance")
}

@Composable
fun ExampleScreen(
    onExampleSelected: (ExampleType) -> Unit
) {
    val examples = listOf(
        ExampleType.ProfileCard,
        ExampleType.ImageReveal,
        ExampleType.TextReveal,
        ExampleType.ButtonEffect,
        ExampleType.LoadingState,
        ExampleType.CardStack,
        ExampleType.ModalReveal
    )

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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "InkFlow Examples",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Explore different use cases",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Show AGSL support status
            Text(
                text = if (InkFlow.isAgslSupported()) {
                    "✓ AGSL Shaders Supported (Full Effect)"
                } else {
                    "⚠ Using Alpha Fade Fallback"
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (InkFlow.isAgslSupported()) {
                    Color(0xFF4CAF50)
                } else {
                    Color(0xFFFF9800)
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(examples) { example ->
                    ExampleCard(
                        example = example,
                        onClick = { onExampleSelected(example) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExampleCard(
    example: ExampleType,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = example.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = example.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
