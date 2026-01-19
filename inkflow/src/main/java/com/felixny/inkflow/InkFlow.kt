@file:JvmName("InkFlow")

package com.felixny.inkflow

import androidx.compose.runtime.Stable

/**
 * InkFlow - A Jetpack Compose UI library for fluid motion effects.
 * 
 * This object serves as the main entry point for the InkFlow library.
 * Use it to access library information, default configurations, and utility functions.
 */
object InkFlow {
    
    /**
     * Library version information.
     */
    const val VERSION_NAME = "1.0.0"
    const val VERSION_CODE = 1
    
    /**
     * Minimum Android API level required for AGSL shader support.
     */
    const val MIN_AGSL_API = 33
    
    /**
     * Default configuration for ink reveal effects.
     * Use this as a starting point or customize with [InkFlowConfig].
     */
    val defaultConfig: InkFlowConfig = InkFlowConfig()
    
    /**
     * Checks if the current device supports AGSL shaders (API 33+).
     * 
     * @return true if AGSL is supported, false otherwise (will use alpha fade fallback)
     */
    fun isAgslSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= MIN_AGSL_API
    }
    
    /**
     * Creates a default configuration optimized for the current device.
     * Automatically adjusts noise scale for tablets.
     * 
     * @param isTablet Whether the device is a tablet (screen width >= 600dp)
     * @return An optimized [InkFlowConfig] instance
     */
    fun createOptimizedConfig(isTablet: Boolean = false): InkFlowConfig {
        return InkFlowConfig(
            noiseScale = if (isTablet) 5.0f else 3.0f,
            distortionStrength = defaultConfig.distortionStrength,
            edgeSoftness = defaultConfig.edgeSoftness
        )
    }
}

/**
 * Configuration class for InkFlow library.
 * 
 * @param noiseScale Controls the scale of the noise pattern (higher = more detail)
 * @param distortionStrength Controls how much the noise distorts the ink spread (0.0 to 1.0)
 * @param edgeSoftness Controls the softness of the ink edge (0.0 to 1.0)
 */
@Stable
data class InkFlowConfig(
    val noiseScale: Float = 3.0f,
    val distortionStrength: Float = 0.15f,
    val edgeSoftness: Float = 0.15f
) {
    init {
        require(noiseScale > 0f) { "noiseScale must be positive" }
        require(distortionStrength in 0f..1f) { "distortionStrength must be between 0 and 1" }
        require(edgeSoftness in 0f..1f) { "edgeSoftness must be between 0 and 1" }
    }
}
