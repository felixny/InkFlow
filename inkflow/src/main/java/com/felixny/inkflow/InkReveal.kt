package com.felixny.inkflow

import android.graphics.RuntimeShader
import android.graphics.RenderEffect
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Extension function that applies an ink bleed reveal effect to a composable.
 * Uses AGSL shaders on API 33+ for organic ink spread, falls back to alpha fade on older devices.
 * 
 * @param progress The reveal progress from 0.0 to 1.0 (will be clamped)
 * @param config Configuration for the ink reveal effect
 * @return A modifier that applies the ink bleed effect
 */
@Composable
fun Modifier.inkReveal(
    progress: Float,
    config: InkFlowConfig = InkFlowConfig()
): Modifier {
    // Clamp progress between 0 and 1
    val clampedProgress = progress.coerceIn(0f, 1f)
    
    // Check if we're on API 33+ for AGSL support
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Cache the RuntimeShader instance to avoid recompilation during recomposition
        val shader = remember(config) {
            RuntimeShader(InkShaders.inkBleedShader)
        }
        
        return this.graphicsLayer {
            // Update all uniforms
            shader.setFloatUniform("progress", clampedProgress)
            shader.setFloatUniform("noiseScale", config.noiseScale)
            shader.setFloatUniform("distortionStrength", config.distortionStrength)
            shader.setFloatUniform("edgeSoftness", config.edgeSoftness)
            
            // Create render effect with the shader using RenderEffect.createRuntimeShaderEffect
            // The inputImage uniform will be automatically set by Compose
            val renderEffect = RenderEffect.createRuntimeShaderEffect(
                shader,
                "inputImage"
            )
            this.renderEffect = renderEffect.asComposeRenderEffect()
        }
    } else {
        // Fallback for API < 33: simple alpha fade
        return this.graphicsLayer {
            alpha = clampedProgress
        }
    }
}
